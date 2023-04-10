package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.Report;
import com.interview.interviewservice.mapper.DTOS.ReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    ReportDTO reportToReportDTO(Report report);

    Report reportDTOToReport(ReportDTO reportDTO);
}
