package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import com.interview.interviewservice.model.ApplicationStatus;
import lombok.Data;


@Data
public class JobApplicationDTO extends BaseDTO {

    private JobDTO jobDTO;

    private CandidateDTO candidateDTO;

    private ApplicationStatus status;

    private CompanyDTO companyDTO;
}
