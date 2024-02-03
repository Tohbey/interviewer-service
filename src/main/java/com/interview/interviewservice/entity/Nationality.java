package com.interview.interviewservice.entity;


import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Nationality extends FlagableAuditableEntity {
    private String description;
    private String code;
    private String dialingCode;

}
