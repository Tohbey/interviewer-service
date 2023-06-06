package com.interview.interviewservice.mapper.DTOS;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
public class EducationalHistoryDTO {
    private Long id;

    private String course;

    private BigInteger yearObtained;

    private String institutionName;

    private String qualification;

    private Long candidateId;

    public String institutionType;

    private Boolean hasDocument;

    private String encodedFile;
}
