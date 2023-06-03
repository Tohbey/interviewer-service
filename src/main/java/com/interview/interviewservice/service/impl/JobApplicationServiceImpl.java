package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.mapper.DTOS.JobApplicationDTO;
import com.interview.interviewservice.service.JobApplicationService;
import com.interview.interviewservice.service.JobTicketService;

import java.util.List;

public class JobApplicationServiceImpl implements JobApplicationService {

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
    public void approveJobApplication(List<Long> ids, String comment) {

    }

    @Override
    public void rejectJobApplication(List<Long> ids, String comment) {

    }
}
