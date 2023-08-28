package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.entity.EmploymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmploymentHistoryRepository extends JpaRepository<EmploymentHistory, Long> {

    List<EmploymentHistory> findAllByCandidate(Candidate candidate);

    void deleteAllByCandidate(Candidate candidate);
}
