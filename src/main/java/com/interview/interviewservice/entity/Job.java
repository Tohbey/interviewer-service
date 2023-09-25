package com.interview.interviewservice.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import com.interview.interviewservice.model.JobType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.NotAudited;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Job extends FlagableAuditableEntity {

    private String title;

    @Column(nullable = false, unique = true)
    private String jobId;

    private String section;

    private String location;

    private String country;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="job_stages", joinColumns = @JoinColumn(name="job_id")
            , inverseJoinColumns = @JoinColumn(name="stages_id"))
    private List<Stage> stages;

    private String description;

    @ElementCollection
    @CollectionTable(name="job_qualification",
            joinColumns = @JoinColumn(name =  "jod_id"))
    private List<String> qualifications;

    @ElementCollection
    @CollectionTable(name="job_responsibility",
            joinColumns = @JoinColumn(name =  "jod_id"))
    private List<String> responsibilities;

    @ElementCollection
    @CollectionTable(name="job_requirement",
            joinColumns = @JoinColumn(name =  "job"))
    private List<String> requirements;

    @ElementCollection
    @CollectionTable(name="job_skills",
            joinColumns = @JoinColumn(name =  "job"))
    private List<String> skills;

    private Date deadline;

    @JoinColumn(name = "company", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @NotAudited
    private Company company;
}
