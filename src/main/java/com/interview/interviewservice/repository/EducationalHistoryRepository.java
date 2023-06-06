package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.entity.EducationalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EducationalHistoryRepository extends JpaRepository<EducationalHistory, Long> {

    List<EducationalHistory> findAllByCandidate(Candidate candidate);
}
