package com.interview.interviewservice.entity.core;

import com.fasterxml.jackson.annotation.JsonView;
import com.interview.interviewservice.model.Model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity implements Model, Serializable {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;

    public static Long defaultIdOf(Long id, BaseEntity entity) {
        return id != null ? id : (entity != null ? entity.getId() : null);
    }
}
