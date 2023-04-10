package com.interview.interviewservice.mapper.mappers;


import com.interview.interviewservice.entity.Job;
import com.interview.interviewservice.mapper.DTOS.JobDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobMapper {
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

    JobDTO jobToJobDTO(Job job);

    Job jobDTOToJob(JobDTO jobDTO);
}
