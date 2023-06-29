package com.interview.interviewservice.entity;


import com.interview.interviewservice.entity.core.BaseEntity;
import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import com.interview.interviewservice.model.ApplicationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class CustomMessage extends FlagableAuditableEntity {

    private String subject;

    @Lob
    private String message;

    @OneToOne
    private Company company;

    private ApplicationStatus status;
}
