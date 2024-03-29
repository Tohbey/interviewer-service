package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.NotAudited;

import java.util.Objects;

@Data
@Entity
public class Stage extends FlagableAuditableEntity {
    private String color;

    private String description;

    @JoinColumn(name = "company", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private Company company;
}
