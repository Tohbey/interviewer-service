package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Team;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.model.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumberIgnoreCase(String phoneNumber);

    List<User> findAllByCompanyAndFlag(Company company, Flag flag);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByIdAndTeams(Long id, Team team);

    List<User> findUserByTeamsContains(Team team);
}
