package com.interview.interviewservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthenticationRequest {

    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message ="Password cant be empty")
    private String password;
}
