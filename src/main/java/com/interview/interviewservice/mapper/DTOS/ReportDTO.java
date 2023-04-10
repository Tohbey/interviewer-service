package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import lombok.Data;

@Data
public class ReportDTO extends BaseDTO {
    private JobDTO job;

    private InterviewDTO interview;

    private UserDTO user;

    private CandidateDTO candidate;

    private Double score;

    private String comment;
}
