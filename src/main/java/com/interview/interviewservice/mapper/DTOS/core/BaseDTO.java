package com.interview.interviewservice.mapper.DTOS.core;

import com.interview.interviewservice.model.Flag;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.util.Date;
@Data
@MappedSuperclass
public abstract class BaseDTO {
    private Long id;

    private Flag flag;

    private String createdBy;

    private Date createdDate;

    private String lastModifiedBy;

    private Date lastModifiedDate;
}
