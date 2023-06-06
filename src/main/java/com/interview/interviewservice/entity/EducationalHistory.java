package com.interview.interviewservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity
public class EducationalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String course;

    private BigInteger yearObtained;

    private String institutionName;

    private String qualification;

    @JsonIgnore
    @JoinColumn(name = "candidate_Id", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Candidate candidate;

    @JsonProperty("institutionType")
    @Transient
    public Long institutionTypeId;

    @Transient
    private Boolean hasDocument;

    @Transient
    private String encodedFile;
}
