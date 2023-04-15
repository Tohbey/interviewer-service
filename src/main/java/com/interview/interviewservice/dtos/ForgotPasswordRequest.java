package com.interview.interviewservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ForgotPasswordRequest {

    @NotEmpty(message ="Old Password cant be empty")
    private String oldPassword;

    @NotEmpty(message ="New Password cant be empty")
    private String newPassword;
}
