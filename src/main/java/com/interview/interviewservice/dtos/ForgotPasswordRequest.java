package com.interview.interviewservice.dtos;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
