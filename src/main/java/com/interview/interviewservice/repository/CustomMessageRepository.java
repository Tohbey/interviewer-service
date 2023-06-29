package com.interview.interviewservice.repository;


import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.CustomMessage;
import com.interview.interviewservice.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomMessageRepository extends JpaRepository<CustomMessage, Long> {

    Boolean existsByCompanyAndStatus(Company company, ApplicationStatus status);

    List<CustomMessage> findAllByCompany(Company company);
}
