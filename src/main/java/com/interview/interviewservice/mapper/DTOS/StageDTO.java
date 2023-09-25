package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import lombok.Data;

@Data
public class StageDTO extends BaseDTO {
    private String color;
    private String description;
    private String companyId;
}
