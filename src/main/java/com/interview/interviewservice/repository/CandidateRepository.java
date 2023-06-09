package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Boolean existsByEmailIgnoreCase(String email);

    Boolean existsByPhoneNumberIgnoreCase(String phoneNumber);
}
