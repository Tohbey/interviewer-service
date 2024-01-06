package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Boolean existsByCompanyIdIgnoreCase(String companyId);

    Company findCompanyByCompanyId(String companyId);

    Boolean existsByContactData_ContactEmailIgnoreCase(String email);

    Boolean existsByContactData_PhoneNumber(String phoneNumber);

}
