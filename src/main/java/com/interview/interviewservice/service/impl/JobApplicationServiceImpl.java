package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Job;
import com.interview.interviewservice.entity.JobApplication;
import com.interview.interviewservice.mapper.DTOS.JobApplicationDTO;
import com.interview.interviewservice.mapper.mappers.JobApplicationMapper;
import com.interview.interviewservice.model.ApplicationStatus;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.CandidateRepository;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.JobApplicationRepository;
import com.interview.interviewservice.repository.JobRepository;
import com.interview.interviewservice.service.*;
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

        Company company = companyRepository.findCompanyByCompanyId(jobApplicationDTO.getCompanyDTO().getCompanyId());
        jobApplication.setCompany(company);
        jobApplication.setStatus(ApplicationStatus.REVIEW);
        jobApplication.setFlag(Flag.ENABLED);

        Optional<Job> job = jobRepository.findById(jobApplicationDTO.getJobDTO().getId());
        Optional<Candidate> candidate = candidateRepository.findById(jobApplicationDTO.getCandidateDTO().getId());

        if(job.isPresent() && candidate.isPresent()){
            jobApplication.setJob(job.get());
            jobApplication.setCandidate(candidate.get());

            jobApplicationRepository.save(jobApplication);
        }
    }

    @Override
    public JobApplicationDTO find(Long jobApplicationId) throws CustomException {
        Optional<JobApplication> jobApplication = jobApplicationRepository.findById(jobApplicationId);
        if(jobApplication.isPresent()){
            return jobApplicationMapper.jobApplicationToJobApplicationDTO(jobApplication.get());
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
    public List<JobApplicationDTO> jobApplicationByCompany(String companyId) throws CustomException {
        List<JobApplicationDTO> jobApplicationDTOS = new ArrayList<>();
        
        Company company = companyRepository.findCompanyByCompanyId(companyId);
        if(Objects.isNull(company)){
            throw new CustomException("Company Info not found");
        }
        
        List<JobApplication> jobApplications = jobApplicationRepository.findAllByCompany(company);

        extracted(jobApplicationDTOS, jobApplications);
        
        return jobApplicationDTOS;
    }

    @Override
    public List<JobApplicationDTO> jobApplicationByJob(Long jobId) throws CustomException {
        List<JobApplicationDTO> jobApplicationDTOS = new ArrayList<>();
        
        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()){
            throw new CustomException("Job Info not found");
        }

        List<JobApplication> jobApplications = jobApplicationRepository.findAllByJob(job.get());

        extracted(jobApplicationDTOS, jobApplications);
        return jobApplicationDTOS;
    }

    @Override
    public List<JobApplicationDTO> jobApplicationByCandidate(Long candidateId) throws CustomException {
        List<JobApplicationDTO> jobApplicationDTOS = new ArrayList<>();
        
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if(candidate.isEmpty()){
            throw new CustomException("candidate Info not found");
        }

        List<JobApplication> jobApplications = jobApplicationRepository.findAllByCandidate(candidate.get());

        extracted(jobApplicationDTOS, jobApplications);
        return jobApplicationDTOS;
    }

    private void extracted(List<JobApplicationDTO> jobApplicationDTOS, List<JobApplication> jobApplications) {
        if(jobApplications.size() > 0){
            jobApplications.forEach(jobApplication -> {
                jobApplicationDTOS.add(jobApplicationMapper.jobApplicationToJobApplicationDTO(jobApplication));
            });
        }
    }

    @Override
    @Transactional
    public void approveJobApplications(List<Long> ids, String comment) {
        ids.forEach(id -> {
            Optional<JobApplication> jobApplication = jobApplicationRepository.findById(id);
            try {
                findAndUpdateJobApplication(comment, jobApplication, ApplicationStatus.ACCEPTED);
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }
        });
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
    public void rejectJobApplications(List<Long> ids, String comment) {
        ids.forEach(id -> {
            Optional<JobApplication> jobApplication = jobApplicationRepository.findById(id);
            try {
                findAndUpdateJobApplication(comment, jobApplication, ApplicationStatus.REJECTED);
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }
        });
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
            throw new CustomException("Job Info is required");
        }

        Optional<Candidate> candidate = candidateRepository.findById(jobApplicationDTO.getCandidateDTO().getId());
        if(candidate.isEmpty()){
            throw new CustomException("Candidate Info cant be found");
        }
        
        if(Objects.isNull(jobApplicationDTO.getCompanyDTO())){
            throw new CustomException("Job Info is required");
        }

        Optional<Company> company = companyRepository.findById(jobApplicationDTO.getCompanyDTO().getId());
        if(company.isEmpty()){
            throw new CustomException("Company Info cant be found");
        }
        
        if(jobApplicationRepository.existsByCandidateAndJobAndStatus(candidate.get(),job.get(), ApplicationStatus.REVIEW)){
            throw new CustomException("You still have an application for this job under review");
        }
    }
    private void JobApplicationProcessEmail(JobApplicationDTO job){

    }
}
