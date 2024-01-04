package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Job;
import com.interview.interviewservice.entity.JobApplication;
import com.interview.interviewservice.mapper.DTOS.JobApplicationDTO;
import com.interview.interviewservice.mapper.DTOS.JobTicketDTO;
import com.interview.interviewservice.mapper.mappers.JobApplicationMapper;
import com.interview.interviewservice.model.ApplicationStatus;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.CandidateRepository;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.JobApplicationRepository;
import com.interview.interviewservice.repository.JobRepository;
import com.interview.interviewservice.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;
    private final UserContextService userContextService;

    @Autowired
    private JobService jobService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private JobTicketService jobTicketService;


    public JobApplicationServiceImpl(JobRepository jobRepository
            , CandidateRepository candidateRepository,
            CompanyRepository companyRepository, JobApplicationRepository jobApplicationRepository,
            JobApplicationMapper jobApplicationMapper, UserContextService userContextService) {
        this.jobRepository = jobRepository;
        this.candidateRepository = candidateRepository;
        this.companyRepository = companyRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobApplicationMapper = jobApplicationMapper;
        this.userContextService = userContextService;
    }

    // send email notification
    @Override
    public void create(JobApplicationDTO jobApplicationDTO) throws CustomException {
        validate(jobApplicationDTO);
        JobApplication jobApplication = jobApplicationMapper.jobApplicationDTOToJobApplication(jobApplicationDTO);

        Company company = companyRepository.findCompanyByCompanyId(jobApplicationDTO.getCompanyId());
        jobApplication.setCompany(company);
        jobApplication.setStatus(ApplicationStatus.REVIEW);
        jobApplication.setFlag(Flag.ENABLED);

        Optional<Job> job = jobRepository.findById(jobApplicationDTO.getJobDTO().getId());
        Optional<Candidate> candidate = candidateRepository.findById(jobApplicationDTO.getCandidateDTO().getId());

        if(job.isPresent() && candidate.isPresent()){
            jobApplication.setJob(job.get());
            jobApplication.setCandidate(candidate.get());

            JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);
            jobApplicationProcessEmail(savedJobApplication);
        }
    }

    @Override
    public JobApplicationDTO find(Long jobApplicationId) throws CustomException {
        JobApplicationDTO jobApplicationDTO = new JobApplicationDTO();
        Optional<JobApplication> jobApplication = jobApplicationRepository.findById(jobApplicationId);
        if(jobApplication.isPresent()){
            jobApplicationDTO = jobApplicationMapper.jobApplicationToJobApplicationDTO(jobApplication.get());

            jobApplicationDTO.setCompanyId(jobApplication.get().getCompany().getCompanyId());
            jobApplicationDTO.setCandidateDTO(candidateService.candidateDtoMapper(jobApplication.get().getCandidate()));
            jobApplicationDTO.setJobDTO(jobService.jobDtoMapper(jobApplication.get().getJob()));
            return jobApplicationDTO;
        }else{
            throw new CustomException("Job Application not found");
        }
    }

    @Override
    public void delete(Long jobApplicationId) throws CustomException {
        Optional<JobApplication> jobApplication = jobApplicationRepository.findById(jobApplicationId);
        if(jobApplication.isPresent()){
            jobApplication.get().setFlag(Flag.DISABLED);
            jobApplicationRepository.save(jobApplication.get());
        }else{
            throw new CustomException("Job Application not found");
        }
    }

    @Override
    public List<JobApplicationDTO> jobApplicationByCompany(String companyId, ApplicationStatus status) throws CustomException {
        List<JobApplicationDTO> jobApplicationDTOS = new ArrayList<>();

        List<JobApplication> jobApplications = new ArrayList<>();

        Company company = companyRepository.findCompanyByCompanyId(companyId);
        if(Objects.isNull(company)){
            throw new CustomException("Company Info not found");
        }

        if(Objects.isNull(status)){
            jobApplications = jobApplicationRepository.findAllByCompany(company);
        }else{
            jobApplications = jobApplicationRepository.findAllByCompanyAndStatus(company, status);
        }

        extracted(jobApplicationDTOS, jobApplications);
        
        return jobApplicationDTOS;
    }

    @Override
    public List<JobApplicationDTO> jobApplicationByJob(Long jobId, ApplicationStatus status) throws CustomException {
        List<JobApplicationDTO> jobApplicationDTOS = new ArrayList<>();

        List<JobApplication> jobApplications = new ArrayList<>();

        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()){
            throw new CustomException("Job Info not found");
        }

        if(Objects.isNull(status)){
            jobApplications = jobApplicationRepository.findAllByJob(job.get());
        }else{
            jobApplications = jobApplicationRepository.findAllByJobAndStatus(job.get(), status);
        }

        extracted(jobApplicationDTOS, jobApplications);
        return jobApplicationDTOS;
    }

    @Override
    public List<JobApplicationDTO> jobApplicationByCandidate(Long candidateId, ApplicationStatus status) throws CustomException {
        List<JobApplicationDTO> jobApplicationDTOS = new ArrayList<>();

        List<JobApplication> jobApplications = new ArrayList<>();

        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if(candidate.isEmpty()){
            throw new CustomException("candidate Info not found");
        }

        if(Objects.isNull(status)){
            jobApplications = jobApplicationRepository.findAllByCandidate(candidate.get());
        }else{
            jobApplications = jobApplicationRepository.findAllByCandidateAndStatus(candidate.get(), status);
        }

        extracted(jobApplicationDTOS, jobApplications);
        return jobApplicationDTOS;
    }

    private void extracted(List<JobApplicationDTO> jobApplicationDTOS, List<JobApplication> jobApplications) {
        if(!jobApplications.isEmpty()){
            jobApplications.forEach(jobApplication -> {
                JobApplicationDTO jobApplicationDTO = new JobApplicationDTO();
                jobApplicationDTO = jobApplicationMapper.jobApplicationToJobApplicationDTO(jobApplication);

                jobApplicationDTO.setCompanyId(jobApplication.getCompany().getCompanyId());
                jobApplicationDTO.setCandidateDTO(candidateService.candidateDtoMapper(jobApplication.getCandidate()));
                jobApplicationDTO.setJobDTO(jobService.jobDtoMapper(jobApplication.getJob()));

                jobApplicationDTOS.add(jobApplicationDTO);
            });
        }
    }

    @Override
    @Transactional
    public void approveJobApplications(List<Long> ids, String comment) throws Exception {
        for(Long id: ids){
            Optional<JobApplication> jobApplication = jobApplicationRepository.findById(id);
            findAndUpdateJobApplication(comment, jobApplication, ApplicationStatus.ACCEPTED);
            createJobTicket(jobApplication);
        }
    }

    private void createJobTicket(Optional<JobApplication> jobApplication) throws Exception {
        JobTicketDTO jobTicketDTO = new JobTicketDTO();

        if(jobApplication.isPresent()){
            jobTicketDTO.setJob(jobService.jobDtoMapper(jobApplication.get().getJob()));
            jobTicketDTO.setCandidate(candidateService.candidateDtoMapper(jobApplication.get().getCandidate()));

            jobTicketService.create(jobTicketDTO);
        }else{
            throw new CustomException("Job Application not found");
        }
    }

//    sending email notification.
    private void findAndUpdateJobApplication(String comment, Optional<JobApplication> jobApplication, ApplicationStatus status) throws CustomException {
        if(jobApplication.isPresent()){
            jobApplication.get().setStatus(status);
            jobApplication.get().setComment(comment);
            jobApplication.get().setLastModifiedDate(new Date());
            jobApplication.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
            jobApplicationRepository.save(jobApplication.get());
        }else{
            throw new CustomException("Job Application not found");
        }
    }

    @Override
    @Transactional
    public void rejectJobApplications(List<Long> ids, String comment) throws CustomException {
        for(Long id: ids){
            Optional<JobApplication> jobApplication = jobApplicationRepository.findById(id);
            findAndUpdateJobApplication(comment, jobApplication, ApplicationStatus.REJECTED);
        }
    }

    private void validate(JobApplicationDTO jobApplicationDTO) throws CustomException {
        if(Objects.isNull(jobApplicationDTO.getJobDTO())){
            throw new CustomException("Job Info is required");
        }

        Optional<Job> job = jobRepository.findById(jobApplicationDTO.getJobDTO().getId());
        if(job.isEmpty()){
            throw new CustomException("Job Info cant be found");    
        }
        
        if(Objects.isNull(jobApplicationDTO.getCandidateDTO())){
            throw new CustomException("Candidate Info is required");
        }

        Optional<Candidate> candidate = candidateRepository.findById(jobApplicationDTO.getCandidateDTO().getId());
        if(candidate.isEmpty()){
            throw new CustomException("Candidate Info cant be found");
        }
        
        if(StringUtils.isEmpty(jobApplicationDTO.getCompanyId())){
            throw new CustomException("Company Info is required");
        }

        Company company = companyRepository.findCompanyByCompanyId(jobApplicationDTO.getCompanyId());
        if(Objects.isNull(company)){
            throw new CustomException("Company Info cant be found");
        }
        
        if(jobApplicationRepository.existsByCandidateAndJobAndStatus(candidate.get(),job.get(), ApplicationStatus.REVIEW)){
            throw new CustomException("You still have an application for this job under review");
        }
    }
    private void jobApplicationProcessEmail(JobApplication job){

    }
}
