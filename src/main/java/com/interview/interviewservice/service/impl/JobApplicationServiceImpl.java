package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.entity.JobApplication;
import com.interview.interviewservice.mapper.DTOS.JobApplicationDTO;
import com.interview.interviewservice.repository.JobApplicationRepository;
import com.interview.interviewservice.service.CandidateService;
import com.interview.interviewservice.service.JobApplicationService;
import com.interview.interviewservice.service.JobService;
import com.interview.interviewservice.service.JobTicketService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobService jobService;

    private final CandidateService candidateService;

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationServiceImpl(JobService jobService, CandidateService candidateService, JobApplicationRepository jobApplicationRepository) {
        this.jobService = jobService;
        this.candidateService = candidateService;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public void create(JobApplicationDTO jobApplicationDTO) {

    }

    @Override
    public JobApplicationDTO find(Long jobApplicationId) {
        return null;
    }

    @Override
    public void update(JobApplicationDTO jobApplicationDTO) {

    }

    @Override
    public void delete(JobApplicationDTO jobApplicationDTO) {

    }

    @Override
    public List<JobTicketService> jobApplicationByCompany(String companyId) {
        return null;
    }

    @Override
    public List<JobTicketService> jobApplicationByJob(Long jobId) {
        return null;
    }

    @Override
    public List<JobTicketService> jobApplicationByCandidate(Long candidate) {
        return null;
    }

    @Override
    public void approveJobApplication(List<Long> ids, String comment) {

    }

    @Override
    public void rejectJobApplication(List<Long> ids, String comment) {

    }

    private void validate(JobApplicationDTO jobApplicationDTO){

    }

    private void validateUpdate(JobApplicationDTO jobApplicationDTO, JobApplication jobApplication){

    }
}
