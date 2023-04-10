package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import com.interview.interviewservice.model.Flag;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.NotAudited;

import java.util.Date;
import java.util.Set;

@Data
@Entity
public class Interview extends FlagableAuditableEntity {

    private Date startDate;
    private Date endDate;

    @OneToOne
    private Stage stage;

    @JoinColumn(name = "company", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private Company company;

    @OneToOne
    private Team team;

    @JoinColumn(name = "job_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Job job;

    private Flag status;
    private String meetingLink;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate", referencedColumnName = "id")
    private Candidate candidate;
}
