package com.interview.interviewservice.mapper.DTOS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseDTO {

    @NotEmpty(message ="surname cant be empty")
    private String surname;

    @NotEmpty(message ="other names cant be empty")
    private String otherNames;

    private String fullname;

    @NotEmpty(message ="address cant be empty")
    private String address;

    @NotEmpty(message ="phone number cant be empty")
    private String phoneNumber;

    @NotEmpty(message ="email cant be empty")
    private String email;

    @Column(name = "image")
    private String userImage;

    private String companyId;

    private Boolean isNewUser;

    @JsonIgnore
    private String password;

    private RoleDTO role;

    private Set<TeamDTO> teamDTO = new HashSet<TeamDTO>();

    private Boolean isActive;
}
