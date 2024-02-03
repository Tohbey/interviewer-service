package com.interview.interviewservice.entity;

import com.interview.interviewservice.entity.core.FlagableAuditableEntity;
import com.interview.interviewservice.model.Model;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Company extends FlagableAuditableEntity {

    private String companyId;
    private String companyName;
    @Embedded
    private ContactData contactData;
    private String picture;
    private String address;

    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Nationality country;


    @Data
    @Embeddable
    public static class ContactData{
        private String phoneNumber;
        private String contactEmail;
        private String surname;
        private String othernames;
    }
}
