package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.mapper.DTOS.JobDTO;
import com.interview.interviewservice.model.Flag;

import java.util.List;

public interface JobService {

    void create(JobDTO jobDTO) throws CustomException;

    JobDTO find(Long jobId) throws CustomException;

    void update(JobDTO jobDTO) throws CustomException;

    void delete(Long jobId) throws CustomException;

    List<JobDTO> findJobsByCompany(Long companyId, Flag flag) throws CustomException;
}
