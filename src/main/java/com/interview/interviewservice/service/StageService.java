package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.mapper.DTOS.StageDTO;
import com.interview.interviewservice.model.Flag;

import java.util.List;

public interface StageService {

    void create(StageDTO stageDTO) throws CustomException;

    StageDTO find(Long stageId) throws CustomException;

    void update(StageDTO stageDTO) throws CustomException;

    void delete(Long stageId) throws CustomException;

    List<StageDTO> findStagesByCompany(String companyId, Flag flag) throws CustomException;
}
