package com.interview.interviewservice.mapper.mappers;


import com.interview.interviewservice.entity.EmploymentHistory;
import com.interview.interviewservice.mapper.DTOS.EmploymentHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmploymentHistoryMapper {

    EmploymentHistoryMapper INSTANCE = Mappers.getMapper(EmploymentHistoryMapper.class);

    EmploymentHistory employmentHistoryDtoToEmploymentHistory(EmploymentHistoryDTO employmentHistoryDTO);

    EmploymentHistoryDTO employmentHistoryToEmploymentHistoryDto(EmploymentHistory employmentHistory);
}
