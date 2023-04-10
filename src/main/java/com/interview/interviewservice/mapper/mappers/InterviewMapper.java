package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.Interview;

import com.interview.interviewservice.entity.core.JpaConverter;
import com.interview.interviewservice.mapper.DTOS.InterviewDTO;
import com.interview.interviewservice.model.Flag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InterviewMapper {
    InterviewMapper INSTANCE = Mappers.getMapper(InterviewMapper.class);

    InterviewDTO interviewToInterviewDTO(Interview interview);

    Interview interviewDTOToInterview(InterviewDTO interviewDTO);

}
