package com.interview.interviewservice.entity;


import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import com.interview.interviewservice.model.ApplicationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class JobApplication extends FlagableAuditableEntity {

    @OneToOne
    private Job job;

    @OneToOne
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String comment;

    @OneToOne
    private Company company;
}
