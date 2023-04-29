package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Invites;
import com.interview.interviewservice.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitesRepository extends JpaRepository<Invites, Long> {

    Boolean existsByEmailAndTeam(String email, Team team);

    Boolean existsByEmail(String email);

    List<Invites> findAllByTeam(Team team);

    Optional<Invites> findByIdAndTeam(Long id, Team team);
}
