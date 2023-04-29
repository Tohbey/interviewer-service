package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class User extends FlagableAuditableEntity {
    private String surname;
    private String otherNames;
    private String password;
    private String address;
    private String phoneNumber;
    private String email;
    @Column(name = "image")
    private String userImage;

    @JoinColumn(name = "company", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private Company company;

//    @JoinColumn(name = "team", referencedColumnName = "id")
//    @ManyToOne(optional = true, fetch = FetchType.EAGER)
//    private Team team;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<Team>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccessedDate;

    private Boolean isNewUser = true;

    private Boolean isActive;

    @OneToOne
    private Role role;
    public String getUserFullName(){
        return this.surname.concat(" ").concat(this.otherNames);
    }
}
