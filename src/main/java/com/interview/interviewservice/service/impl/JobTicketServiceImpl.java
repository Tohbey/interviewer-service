package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.entity.Job;
import com.interview.interviewservice.entity.JobTicket;
import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.mapper.DTOS.JobTicketDTO;
import com.interview.interviewservice.mapper.mappers.JobTicketMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.CandidateRepository;
import com.interview.interviewservice.repository.JobRepository;
import com.interview.interviewservice.repository.JobTicketRepository;
import com.interview.interviewservice.repository.StageRepository;
import com.interview.interviewservice.service.JobTicketService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class JobTicketServiceImpl implements JobTicketService {

    private final JobTicketRepository jobTicketRepository;

    private final JobTicketMapper jobTicketMapper;

    private final JobRepository jobRepository;

    private final CandidateRepository candidateRepository;

    private final StageRepository stageRepository;

    public JobTicketServiceImpl(JobTicketRepository jobTicketRepository, JobTicketMapper jobTicketMapper, JobRepository jobRepository, CandidateRepository candidateRepository, StageRepository stageRepository) {
        this.jobTicketRepository = jobTicketRepository;
        this.jobTicketMapper = jobTicketMapper;
        this.jobRepository = jobRepository;
        this.candidateRepository = candidateRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    public void create(JobTicketDTO jobTicketDTO) throws CustomException {
        validate(jobTicketDTO);
        JobTicket jobTicket = jobTicketMapper.jobTicketDTOToJobTicket(jobTicketDTO);
        jobTicket.setFlag(Flag.ENABLED);

        Optional<Stage> stage = stageRepository.findById(jobTicketDTO.getCurrentStage().getId());
        Optional<Job> job = jobRepository.findById(jobTicketDTO.getJob().getId());
        Optional<Candidate> candidate = candidateRepository.findById(jobTicketDTO.getCandidate().getId());

        if(stage.isPresent() && job.isPresent() && candidate.isPresent()) {

            jobTicket.setCandidate(candidate.get());
            jobTicket.setJob(job.get());
            jobTicket.setCurrentStage(stage.get());

            jobTicketRepository.save(jobTicket);
        }
    }

    @Override
    public JobTicketDTO find(Long jobTicketId) throws CustomException {
        Optional<JobTicket> jobTicket = jobTicketRepository.findById(jobTicketId);
        if(jobTicket.isPresent()) {
            return jobTicketMapper.jobTicketToJobTicketDTO(jobTicket.get());
        }else{
            throw new CustomException("Ticket not found");
        }
    }

    @Override
    public void update(JobTicketDTO jobTicketDTO) {

    }

    @Override
    public void delete(Long jobTicketId) throws CustomException {
        Optional<JobTicket> jobTicket = jobTicketRepository.findById(jobTicketId);
        if(jobTicket.isPresent()) {
           jobTicket.get().setFlag(Flag.DISABLED);
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
            tickets.forEach(ticket -> {
                jobTickets.add(jobTicketMapper.jobTicketToJobTicketDTO(ticket));
            });

        }else{
            throw new CustomException("Job not found");
        }

        return jobTickets;
    }

    private void validate(JobTicketDTO jobTicketDTO) throws CustomException {
        if(Objects.isNull(jobTicketDTO)){
            throw new CustomException("JobTicketDTO cant be null");
        }

        if (Objects.nonNull(jobTicketDTO.getJob())) {
            throw new CustomException("Current Stage cannot be null");
        }
        Optional<Job> job = jobRepository.findById(jobTicketDTO.getJob().getId());
        if(job.isEmpty()){
            throw new CustomException("Job not found");
        }

        if (Objects.nonNull(jobTicketDTO.getCandidate())) {
            throw new CustomException("Current Stage cannot be null");
        }
        Optional<Candidate> candidate = candidateRepository.findById(jobTicketDTO.getCandidate().getId());
        if(candidate.isEmpty()){
            throw new CustomException("Candidate not found");
        }

        if(jobTicketRepository.existsByCandidateAndJobAndFlag(candidate.get(), job.get(), Flag.ENABLED)){
            throw new CustomException("Ticket already exist for this candidate");
        }

        if (Objects.nonNull(jobTicketDTO.getCurrentStage())) {
            throw new CustomException("Current Stage cannot be null");
        }

        Optional<Stage> stage = stageRepository.findById(jobTicketDTO.getCurrentStage().getId());
        if(stage.isEmpty()){
            throw new CustomException("Stage not found");
        }
    }

    private void validateUpdate(JobTicketDTO jobTicketDTO, JobTicket savedJobTicket){

    }
}
