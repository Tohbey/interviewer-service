package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Job;
import com.interview.interviewservice.model.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Boolean existsByTitleAndCompany(String title, Company company);

    List<Job> findAllByCompanyAndFlag(Company company, Flag flag);
}
