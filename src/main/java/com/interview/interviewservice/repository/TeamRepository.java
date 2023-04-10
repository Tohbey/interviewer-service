package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>{

    Boolean existsByNameAndCompany(String name, Company company);

    Boolean existsBySectionAndCompany(String section, Company company);

    List<Team> findTeamsByCompany(Company company);
}
