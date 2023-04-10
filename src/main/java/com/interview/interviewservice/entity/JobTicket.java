package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class JobTicket extends FlagableAuditableEntity {

    @OneToMany
    private List<Interview> interviews;

    @OneToMany
    private List<Report> reports;

    @OneToOne
    private Candidate candidate;

    @OneToOne
    private Stage currentStage;

    @OneToMany
    private List<Stage> stages;
}
