package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.PasswordRetrieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRetrieveRepository extends JpaRepository<PasswordRetrieve, Long> {

    Optional<PasswordRetrieve> findPasswordRetrieveByResetPasswordToken(String token);

}
