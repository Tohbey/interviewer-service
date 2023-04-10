package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.NotAudited;

import java.util.Date;

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

    @JoinColumn(name = "team", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private Team team;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccessedDate;

    private Boolean isNewUser = true;

    @OneToOne
    private Role role;
    public String getUserFullName(){
        return this.surname.concat(" ").concat(this.otherNames);
    }
}
