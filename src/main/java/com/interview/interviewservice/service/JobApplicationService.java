package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.mapper.DTOS.JobApplicationDTO;

import java.util.List;

public interface JobApplicationService {

    void create(JobApplicationDTO jobApplicationDTO) throws CustomException;

    JobApplicationDTO find(Long jobApplicationId) throws CustomException;

    void delete(Long jobApplicationId) throws CustomException;

    List<JobApplicationDTO> jobApplicationByCompany(String companyId) throws CustomException;

    List<JobApplicationDTO> jobApplicationByJob(Long jobId) throws CustomException;

    List<JobApplicationDTO> jobApplicationByCandidate(Long candidateId) throws CustomException;

    void approveJobApplications(List<Long> ids, String comment) throws Exception;

    void rejectJobApplications(List<Long> ids, String comment) throws CustomException;
}
