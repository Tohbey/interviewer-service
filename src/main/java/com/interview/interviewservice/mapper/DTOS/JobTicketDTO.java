package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import lombok.Data;

import java.util.List;

@Data
public class JobTicketDTO extends BaseDTO {
    private List<InterviewDTO> interviews;

    private List<ReportDTO>  reports;

    private CandidateDTO candidate;

    private StageDTO currentStage;

    private List<StageDTO> stages;

    private JobDTO job;
}
