package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.model.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long>, JpaSpecificationExecutor<Stage> {

    Boolean existsByDescriptionAndCompany(String description, Company company);

    List<Stage> findAllByCompanyAndFlag(Company company, Flag flag);

    Optional<Stage> findByIdAndCompany(Long id, Company company);
}
