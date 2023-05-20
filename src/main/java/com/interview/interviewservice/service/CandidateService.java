package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.mapper.DTOS.CandidateDTO;
import jakarta.mail.MessagingException;

public interface CandidateService {

    void create(CandidateDTO candidateDTO) throws CustomException, MessagingException;

    void delete(Long candidateId) throws Exception;

    CandidateDTO find(Long candidateId) throws Exception;

    void update(CandidateDTO candidateDTO) throws CustomException;
}
