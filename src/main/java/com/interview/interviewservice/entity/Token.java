package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Token extends BaseEntity {

    private String token;
    @Column(name = "expired_at")
    private Date expiredAt;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
