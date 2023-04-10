package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.model.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {

    Boolean existsByDescriptionAndCompany(String description, Company company);

    List<Stage> findAllByCompanyAndFlag(Company company, Flag flag);
}
