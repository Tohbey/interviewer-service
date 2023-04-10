package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Candidate extends FlagableAuditableEntity {
    private String surname;
    private String otherNames;
    private String password;
    private String address;
    private String phoneNumber;
    private String email;
    @Column(name = "image")
    private String userImage;

    private String linkedln;
    private String resume;

    @ElementCollection
    @CollectionTable(name="links",
            joinColumns = @JoinColumn(name =  "candidate"))
    private Set<String> links = new HashSet<>();

    private String getUserFullName(){
        return this.surname.concat(" ").concat(this.otherNames);
    }
}
