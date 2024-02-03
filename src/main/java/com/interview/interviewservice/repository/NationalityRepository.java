package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationalityRepository extends JpaRepository<Nationality, Long> {
}
