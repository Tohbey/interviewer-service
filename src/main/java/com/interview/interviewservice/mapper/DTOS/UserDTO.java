package com.interview.interviewservice.mapper.DTOS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotEmpty(message ="company cant be empty")
    private String companyId;

    private Boolean isNewUser;

    @JsonIgnore
    private String password;

    private RoleDTO role;

    private TeamDTO teamDTO;

    private Boolean isActive;
}
