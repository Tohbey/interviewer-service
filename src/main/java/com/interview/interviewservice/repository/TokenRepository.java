package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Token;
import com.interview.interviewservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTokenByTokenAndUser(String token, User user);

    Optional<Token> findTokenByUser(User user);
}
