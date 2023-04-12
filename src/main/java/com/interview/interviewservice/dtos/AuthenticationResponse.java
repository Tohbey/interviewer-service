package com.interview.interviewservice.dtos;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String jwt;
}
