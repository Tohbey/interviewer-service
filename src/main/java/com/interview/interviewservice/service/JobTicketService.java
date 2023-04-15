package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.mapper.DTOS.JobTicketDTO;

import java.util.List;

public interface JobTicketService {

    void create(JobTicketDTO jobTicketDTO) throws CustomException;

    JobTicketDTO find(Long jobTicketId) throws CustomException;

    void update(JobTicketDTO jobTicketDTO);

    void delete(Long jobTicketDTO) throws CustomException;

    List<JobTicketDTO> jobTicketsByJob(Long jobId) throws CustomException;
}
