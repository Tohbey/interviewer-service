package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.dtos.AuthenticationResponse;
import com.interview.interviewservice.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(long userId);

    RefreshToken verifyExpiration(RefreshToken refreshToken) throws CustomException;

    int deleteByUser(Long userId);

    AuthenticationResponse refresh(String token) throws CustomException;
}
