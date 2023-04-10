package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Report extends FlagableAuditableEntity {

    @OneToOne
    private Job job;

    @OneToOne
    private Interview interview;

    @ManyToOne
    private User user;

    @OneToOne
    private Candidate candidate;

    private Double score;

    private String comment;
}
