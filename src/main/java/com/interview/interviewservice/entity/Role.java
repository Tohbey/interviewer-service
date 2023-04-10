package com.interview.interviewservice.entity;

import com.interview.interviewservice.model.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
}
