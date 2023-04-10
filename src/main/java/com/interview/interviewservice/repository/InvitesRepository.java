package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Invites;
import com.interview.interviewservice.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitesRepository extends JpaRepository<Invites, Long> {

    Boolean existsByEmailAndTeam(String email, Long id);

    Boolean existsByEmail(String email);

    List<Invites> findAllByTeam(Team team);
}
