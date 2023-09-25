package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.mapper.DTOS.CompanyDTO;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.model.Flag;
import jakarta.mail.MessagingException;

import java.util.List;

public interface CompanyService {

    void create(CompanyDTO companyDTO) throws CustomException, MessagingException;

    void delete(Long companyId) throws Exception;

    CompanyDTO find(String companyId) throws CustomException;

    void update(CompanyDTO companyDTO) throws CustomException;

    List<TeamDTO> findTeamsByCompany(String companyId) throws CustomException;
}
