package com.interview.interviewservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Entity
@Data
public class EmploymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    private String companyName;

    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;

    private String employmentType;

    @JsonIgnore
    @JoinColumn(name = "candidate_Id", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Candidate candidate;
}
