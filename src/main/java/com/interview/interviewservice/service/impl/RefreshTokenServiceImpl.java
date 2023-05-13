package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.dtos.AuthenticationResponse;
import com.interview.interviewservice.entity.RefreshToken;
import com.interview.interviewservice.jwt.JwtUtils;
import com.interview.interviewservice.repository.RefreshTokenRepository;
import com.interview.interviewservice.repository.UserRepository;
import com.interview.interviewservice.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private Integer refreshTokenDurationMs = 86400000;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Autowired
    private JwtUtils jwtTokenUtils;

    @Override
    public RefreshToken createRefreshToken(long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken refreshToken) throws CustomException {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new CustomException("Failed For "+refreshToken.getToken()+ " Refresh token was expired. Please Login");
        }

        return refreshToken;
    }

    @Override
    @Transactional
    public int deleteByUser(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    @Override
    public AuthenticationResponse refresh(String token) throws CustomException {
      Optional<RefreshToken> refreshToken =  findByToken(token);
      if(refreshToken.isPresent()) {
          verifyExpiration(refreshToken.get());
      }else{
          throw  new CustomException("Refresh Token Info not found");
      }

      return new AuthenticationResponse(jwtTokenUtils.generateTokenFromUsername(refreshToken.get().getUser().getEmail()),
              token, "bearer");
    }
}
