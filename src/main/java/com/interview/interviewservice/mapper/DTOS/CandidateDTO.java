package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import jakarta.persistence.*;
import lombok.Data;

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

    private Set<EducationalHistoryDTO> educationalHistories = new HashSet<>();

    private Set<EmploymentHistoryDTO> employmentHistories = new HashSet<>();

    private String setFullName(){
       return this.surname.concat(" ").concat(this.otherNames);
    }
}
