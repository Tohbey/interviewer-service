package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

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

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name="user_team", joinColumns = @JoinColumn(name="user_id")
            , inverseJoinColumns = @JoinColumn(name="team_id"))
    private List<Team> teams = new ArrayList<>();

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
