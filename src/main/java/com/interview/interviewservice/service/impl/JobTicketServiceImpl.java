package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.*;
import com.interview.interviewservice.mapper.DTOS.InterviewDTO;
import com.interview.interviewservice.mapper.DTOS.JobTicketDTO;
import com.interview.interviewservice.mapper.DTOS.ReportDTO;
import com.interview.interviewservice.mapper.DTOS.StageDTO;
import com.interview.interviewservice.mapper.mappers.JobTicketMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.*;
import com.interview.interviewservice.service.CandidateService;
import com.interview.interviewservice.service.JobService;
import com.interview.interviewservice.service.JobTicketService;
import com.interview.interviewservice.service.UserContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobTicketServiceImpl implements JobTicketService {

    private final JobTicketRepository jobTicketRepository;

    private final JobTicketMapper jobTicketMapper;

    private final JobRepository jobRepository;

    private final CandidateRepository candidateRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private JobService jobService;

    private final StageRepository stageRepository;

    private final UserContextService userContextService;

    private final InterviewRepository interviewRepository;

    private final ReportRepository reportRepository;

    public JobTicketServiceImpl(JobTicketRepository jobTicketRepository, JobTicketMapper jobTicketMapper, JobRepository jobRepository,
                                CandidateRepository candidateRepository, StageRepository stageRepository, UserContextService userContextService, InterviewRepository interviewRepository, ReportRepository reportRepository) {
        this.jobTicketRepository = jobTicketRepository;
        this.jobTicketMapper = jobTicketMapper;
        this.jobRepository = jobRepository;
        this.candidateRepository = candidateRepository;
        this.stageRepository = stageRepository;
        this.userContextService = userContextService;
        this.interviewRepository = interviewRepository;
        this.reportRepository = reportRepository;
    }

    @Override
    public void create(JobTicketDTO jobTicketDTO) throws CustomException {
        validate(jobTicketDTO);
        JobTicket jobTicket = mapper(jobTicketDTO);
        jobTicket.setFlag(Flag.ENABLED);

        Optional<Job> job = jobRepository.findById(jobTicketDTO.getJob().getId());
        Optional<Candidate> candidate = candidateRepository.findById(jobTicketDTO.getCandidate().getId());

        if(job.isPresent() && candidate.isPresent()) {

            jobTicket.setCandidate(candidate.get());
            jobTicket.setJob(job.get());
            jobTicket.setCurrentStage(job.get().getStages().get(0));

            jobTicketRepository.save(jobTicket);
        }
    }

    @Override
    public JobTicketDTO find(Long jobTicketId) throws CustomException {
        Optional<JobTicket> jobTicket = jobTicketRepository.findById(jobTicketId);
        if(jobTicket.isPresent()) {
            JobTicketDTO jobTicketDTO = jobTicketMapper.jobTicketToJobTicketDTO(jobTicket.get());
            jobTicketDTO.setCandidate(candidateService.candidateDtoMapper(jobTicket.get().getCandidate()));
            jobTicketDTO.setJob(jobService.jobDtoMapper(jobTicket.get().getJob()));
            return jobTicketDTO;
        }else{
            throw new CustomException("Ticket not found");
        }
    }

    @Override
    public void update(JobTicketDTO jobTicketDTO) throws CustomException {
        Optional<JobTicket> savedJobTicket = jobTicketRepository.findById(jobTicketDTO.getId());
        if(savedJobTicket.isEmpty()){
            throw new CustomException("Job ticket Doesnt Exist");
        }
        validateUpdate(jobTicketDTO, savedJobTicket.get());

        JobTicket jobTicket = mapper(jobTicketDTO);
        jobTicket.setFlag(savedJobTicket.get().getFlag());

        Optional<Job> job = jobRepository.findById(jobTicketDTO.getJob().getId());
        Optional<Candidate> candidate = candidateRepository.findById(jobTicketDTO.getCandidate().getId());

        if(job.isPresent() && candidate.isPresent()) {

            jobTicket.setCandidate(candidate.get());
            jobTicket.setJob(job.get());

            Optional<Stage> currentStage = stageRepository.findByIdAndCompany(jobTicketDTO.getCurrentStage().getId(), savedJobTicket.get().getJob().getCompany());
            currentStage.ifPresent(jobTicket::setCurrentStage);

            for(StageDTO stageDTO : jobTicketDTO.getPreviousStages()){
                jobTicket.setPreviousStages(new ArrayList<>());
                Optional<Stage> stage = stageRepository.findByIdAndCompany(stageDTO.getId(), savedJobTicket.get().getJob().getCompany());
                stage.ifPresent(value -> jobTicket.getPreviousStages().add(value));
            }

            jobTicketRepository.save(jobTicket);
        }
    }

    @Override
    public void delete(Long jobTicketId) throws CustomException {
        Optional<JobTicket> jobTicket = jobTicketRepository.findById(jobTicketId);
        if(jobTicket.isPresent()) {
           jobTicket.get().setFlag(Flag.DISABLED);
           jobTicket.get().setLastModifiedDate(new Date());
           jobTicket.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
           jobTicketRepository.save(jobTicket.get());
        }else{
            throw new CustomException("Ticket not found");
        }
    }

    @Override
    public List<JobTicketDTO> jobTicketsByJob(Long jobId) throws CustomException {
        List<JobTicketDTO> jobTickets = new ArrayList<>();
        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isPresent()) {
            List<JobTicket> tickets = jobTicketRepository.findAllByJob(job.get());
            for(JobTicket ticket: tickets){
                JobTicketDTO jobTicketDTO = jobTicketMapper.jobTicketToJobTicketDTO(ticket);
                jobTicketDTO.setCandidate(candidateService.candidateDtoMapper(ticket.getCandidate()));

                jobTickets.add(jobTicketDTO);
            }
        }else{
            throw new CustomException("Job not found");
        }

        return jobTickets;
    }

    private void validate(JobTicketDTO jobTicketDTO) throws CustomException {
        if(Objects.isNull(jobTicketDTO)){
            throw new CustomException("JobTicketDTO cant be null");
        }

        if (Objects.isNull(jobTicketDTO.getJob())) {
            throw new CustomException("Job info cannot be null");
        }

        Optional<Job> job = jobRepository.findById(jobTicketDTO.getJob().getId());
        if(job.isEmpty()){
            throw new CustomException("Job not found");
        }

        if (Objects.isNull(jobTicketDTO.getCandidate())) {
            throw new CustomException("Candidate cannot be null");
        }
        Optional<Candidate> candidate = candidateRepository.findById(jobTicketDTO.getCandidate().getId());
        if(candidate.isEmpty()){
            throw new CustomException("Candidate not found");
        }

        if(jobTicketRepository.existsByCandidateAndJobAndFlag(candidate.get(), job.get(), Flag.ENABLED)){
            throw new CustomException("Ticket already exist for this candidate");
        }
    }

    private void validateUpdate(JobTicketDTO jobTicketDTO, JobTicket savedJobTicket) throws CustomException {
        if(Objects.isNull(jobTicketDTO) || Objects.isNull(savedJobTicket)){
            throw new CustomException("Job Ticket cant be null");
        }

        if (Objects.isNull(jobTicketDTO.getJob()) || Objects.isNull(savedJobTicket.getJob())) {
            throw new CustomException("Job info cannot be null");
        }

        Optional<Job> job = jobRepository.findById(jobTicketDTO.getJob().getId());
        if(job.isEmpty()){
            throw new CustomException("Job not found");
        }

        if(!job.get().equals(savedJobTicket.getJob())){
            throw new CustomException("Job Position cant be changed");
        }

        if (Objects.isNull(jobTicketDTO.getCandidate())) {
            throw new CustomException("Candidate cannot be null");
        }else{
            Optional<Candidate> candidate = candidateRepository.findById(jobTicketDTO.getCandidate().getId());
            if(candidate.isEmpty()){
                throw new CustomException("Candidate not found");
            }
            if(!candidate.get().equals(savedJobTicket.getCandidate())){
                throw new CustomException("Candidate cant be changed");
            }
        }

        if(Objects.nonNull(jobTicketDTO.getCurrentStage())){
            Optional<Stage> currentStage = stageRepository.findByIdAndCompany(jobTicketDTO.getCurrentStage().getId(), savedJobTicket.getJob().getCompany());
            if(currentStage.isEmpty()){
                throw new CustomException("Current stage cant be empty");
            }else{
                if(!currentStage.get().equals(savedJobTicket.getCurrentStage())
                        && jobTicketDTO.getPreviousStages().isEmpty()){
                    throw new CustomException("Previous stage cant be empty");
                }
            }

        }

        if(!jobTicketDTO.getPreviousStages().isEmpty()){
            for(StageDTO stageDTO : jobTicketDTO.getPreviousStages()){
                Optional<Stage> stage = stageRepository.findByIdAndCompany(stageDTO.getId(), savedJobTicket.getJob().getCompany());
                if(stage.isEmpty()){
                    throw new CustomException("Previous Stage not attached to this company");
                }
            }
        }

        if(!jobTicketDTO.getInterviews().isEmpty()){
            for(InterviewDTO interviewDTO : jobTicketDTO.getInterviews()){
                Optional<Interview> interview = interviewRepository.findById(interviewDTO.getId());
                if(interview.isEmpty()){
                    throw new CustomException("Interview Information not found");
                }
            }
        }

        if(!jobTicketDTO.getReports().isEmpty()){
            for(ReportDTO reportDTO : jobTicketDTO.getReports()){
                Optional<Report> report = reportRepository.findById(reportDTO.getId());
                if(report.isEmpty()){
                    throw new CustomException("Report Information not found");
                }
            }
        }
    }

    private JobTicket mapper(JobTicketDTO jobTicketDTO) throws CustomException {
        JobTicket jobTicket = jobTicketMapper.jobTicketDTOToJobTicket(jobTicketDTO);
        if(Objects.isNull(jobTicket.getId())){
            jobTicket.setCreatedDate(new Date());
            jobTicket.setCreatedBy(userContextService.getCurrentUserDTO().getFullname());
        }else{
            jobTicket.setLastModifiedDate(new Date());
            jobTicket.setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
        }

        return jobTicket;
    }
}
