package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import com.interview.interviewservice.model.Flag;
import lombok.Data;

import java.util.Date;

@Data
public class InterviewDTO extends BaseDTO {
    private Date startDate;

    private Date endDate;

    private StageDTO stage;

    private String companyId;

    private TeamDTO team;

    private JobDTO job;

    private Flag status;

    private String meetingLink;

    private CandidateDTO candidate;
}
