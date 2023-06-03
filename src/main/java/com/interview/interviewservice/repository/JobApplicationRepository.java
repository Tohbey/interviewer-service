package com.interview.interviewservice.repository;


import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Job;
import com.interview.interviewservice.entity.JobApplication;
import com.interview.interviewservice.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Boolean existsByCandidateAndJobAndStatus(Candidate candidate, Job job, ApplicationStatus status);

    List<JobApplication> findAllByJob(Job job);

    List<JobApplication> findAllByCompany(Company company);

    List<JobApplication> findAllByCandidate(Candidate candidate);
}
