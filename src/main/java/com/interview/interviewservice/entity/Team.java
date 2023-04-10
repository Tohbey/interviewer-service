package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.NotAudited;

@Data
@Entity
public class Team extends FlagableAuditableEntity {

    private String name;

    private String color;

    private String section;

    @JoinColumn(name = "company", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @NotAudited
    private Company company;
}
