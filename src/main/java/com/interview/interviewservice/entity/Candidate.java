package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Candidate extends FlagableAuditableEntity {

    private String surname;

    private String otherNames;

    private String address;

    private String phoneNumber;

    private String email;

    @Column(name = "image")
    private String userImage;

    private String linkedln;

    private String resume;

    @OneToOne
    private User user;

    private String getUserFullName(){
        return this.surname.concat(" ").concat(this.otherNames);
    }
}
