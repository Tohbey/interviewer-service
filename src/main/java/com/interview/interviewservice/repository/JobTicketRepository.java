package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.JobTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTicketRepository extends JpaRepository<JobTicket, Long> {
}
