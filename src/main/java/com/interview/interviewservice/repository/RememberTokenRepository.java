package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.RememberToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RememberTokenRepository extends JpaRepository<RememberToken, Long> {

    Optional<RememberToken> findRememberTokenByToken(String token);
}
