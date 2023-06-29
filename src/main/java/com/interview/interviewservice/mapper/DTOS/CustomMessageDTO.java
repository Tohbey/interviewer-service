package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import com.interview.interviewservice.model.ApplicationStatus;
import lombok.Data;

@Data
public class CustomMessageDTO extends BaseDTO {
    private String subject;

    private String message;

    private String companyId;

    private ApplicationStatus status;
}
