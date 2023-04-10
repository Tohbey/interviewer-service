package com.interview.interviewservice.mapper.DTOS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class CandidateDTO extends BaseDTO {

    private String surname;

    private String otherNames;

    private String fullName;

    private String address;

    private String phoneNumber;

    private String email;

    @Column(name = "image")
    private String userImage;

    private String linkedln;

    private String resume;

    private Set<String> links = new HashSet<>();

}
