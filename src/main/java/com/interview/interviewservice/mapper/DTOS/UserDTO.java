package com.interview.interviewservice.mapper.DTOS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseDTO {
    private String surname;

    private String otherNames;

    private String fullname;

    private String address;

    private String phoneNumber;

    private String email;

    @Column(name = "image")
    private String userImage;

    private String companyId;

    private Boolean isNewUser;

    @JsonIgnore
    private String password;

    private RoleDTO role;

    private TeamDTO teamDTO;
}
