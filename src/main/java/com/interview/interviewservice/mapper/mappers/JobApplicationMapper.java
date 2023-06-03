package com.interview.interviewservice.mapper.mappers;


import com.interview.interviewservice.entity.JobApplication;
import com.interview.interviewservice.mapper.DTOS.JobApplicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobApplicationMapper {

    JobApplicationMapper INSTANCE = Mappers.getMapper(JobApplicationMapper.class);

    JobApplicationDTO jobApplicationToJobApplicationDTO(JobApplication jobApplication);

    JobApplication jobApplicationDTOToJobApplication(JobApplicationDTO jobApplicationDTO);
}
