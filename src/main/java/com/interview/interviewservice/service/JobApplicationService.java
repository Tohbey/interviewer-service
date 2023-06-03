package com.interview.interviewservice.service;

import com.interview.interviewservice.mapper.DTOS.JobApplicationDTO;

import java.util.List;

public interface JobApplicationService {

    void create(JobApplicationDTO jobApplicationDTO);

    JobApplicationDTO find(Long jobApplicationId);

    void update(JobApplicationDTO jobApplicationDTO);

    void delete(JobApplicationDTO jobApplicationDTO);

    List<JobTicketService> jobApplicationByCompany(String companyId);

    List<JobTicketService> jobApplicationByJob(Long jobId);

    List<JobTicketService> jobApplicationByCandidate(Long candidate);

    void approveJobApplication(List<Long> ids, String comment);

    void rejectJobApplication(List<Long> ids, String comment);

}
