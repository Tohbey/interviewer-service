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
    private String jobId;

    private String section;

    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private String workType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Stage> stages;

    private String description;

    @ElementCollection
    @CollectionTable(name="qualification",
            joinColumns = @JoinColumn(name =  "jod_id"))
    private Set<String> qualifications = new HashSet<>();

    @ElementCollection
    @CollectionTable(name="responsibility",
            joinColumns = @JoinColumn(name =  "jod_id"))
    private Set<String> responsibilities = new HashSet<>();

    @ElementCollection
    @CollectionTable(name="requirement",
            joinColumns = @JoinColumn(name =  "job"))
    private Set<String> requirements = new HashSet<>();

    private Date deadline;

    @JoinColumn(name = "company", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @NotAudited
    private Company company;

    private void setJobId(){
        this.jobId = "#".concat(this.id.toString()).concat(this.company.getCompanyId());
    }
}
