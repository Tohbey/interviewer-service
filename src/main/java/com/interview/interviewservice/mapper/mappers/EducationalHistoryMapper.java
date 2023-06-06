package com.interview.interviewservice.mapper.mappers;


import com.interview.interviewservice.entity.EducationalHistory;
import com.interview.interviewservice.mapper.DTOS.EducationalHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EducationalHistoryMapper {
    EducationalHistoryMapper INSTANCE = Mappers.getMapper(EducationalHistoryMapper.class);

    EducationalHistoryDTO educationHistoryToEducationHistoryDto(EducationalHistory educationalHistory);

    EducationalHistory educationHistoryDtoToEducationHistory(EducationalHistoryDTO educationalHistoryDTO);

}
