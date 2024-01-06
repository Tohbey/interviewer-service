package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Job;
import com.interview.interviewservice.model.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    Boolean existsByTitleAndCompany(String title, Company company);

    List<Job> findAllByCompanyAndFlag(Company company, Flag flag);

    List<Job> findAllByCompany(Company company);

    Optional<Job> findByJobId(String jobId);
}
