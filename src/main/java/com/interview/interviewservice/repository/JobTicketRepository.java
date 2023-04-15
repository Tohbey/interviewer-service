package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.entity.Job;
import com.interview.interviewservice.entity.JobTicket;
import com.interview.interviewservice.model.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobTicketRepository extends JpaRepository<JobTicket, Long> {

    Boolean existsByCandidateAndJobAndFlag(Candidate candidate, Job job, Flag flag);

    List<JobTicket> findAllByJob(Job job);
}
