package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.mapper.DTOS.InterviewDTO;

import java.util.List;

public interface InterviewService {

    void create(InterviewDTO interviewDTO) throws CustomException;

    InterviewDTO findInterview(Long interviewId) throws CustomException;

    void delete(Long interviewId) throws CustomException;

    void update(InterviewDTO interviewDTO) throws CustomException;

    List<InterviewDTO> findInterviewsByJob(Long jobId) throws CustomException;

    List<InterviewDTO> findInterviewsByTeam(Long teamId) throws CustomException;

    InterviewDTO findInterviewByStageAndCandidate(Long stageId, Long candidateId) throws CustomException;
}
