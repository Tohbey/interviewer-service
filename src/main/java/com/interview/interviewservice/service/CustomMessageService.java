package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.mapper.DTOS.CustomMessageDTO;

import java.util.List;

public interface CustomMessageService {

    void create(CustomMessageDTO customMessageDTO) throws CustomException;

    CustomMessageDTO findCustomMessage(Long customMessageId) throws CustomException;

    List<CustomMessageDTO> findCustomMessagesByCompanyId(String companyId) throws CustomException;

    void update(CustomMessageDTO customMessageDTO) throws CustomException;

    void delete(Long customMessageId) throws CustomException;
}
