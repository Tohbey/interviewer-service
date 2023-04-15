package com.interview.interviewservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Token cant be empty")
    private String token;

    @NotEmpty(message ="Password cant be empty")
    private String password;
}
