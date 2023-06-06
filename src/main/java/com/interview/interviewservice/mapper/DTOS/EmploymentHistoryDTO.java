package com.interview.interviewservice.mapper.DTOS;

import lombok.Data;

import java.util.Date;


@Data
public class EmploymentHistoryDTO {
    private Long id;

    private String title;

    private String companyName;

    private String location;

    private Date fromDate;

    private Date toDate;

    private String employmentType;

    private Long candidateId;
}
