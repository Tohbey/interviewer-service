package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.*;
import com.interview.interviewservice.model.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long>{

    Boolean existsByTeamAndStageAndFlagAndCandidate(Team team, Stage stage, Flag flag, Candidate candidate);

    List<Interview> findAllByJob(Job job);

    List<Interview> findAllByTeam(Team team);

    Interview findAllByCandidateAndStage(Candidate candidate, Stage stage);
}
