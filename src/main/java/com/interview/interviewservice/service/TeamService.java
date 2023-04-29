package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.dtos.TeamMemberAndInvite;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import jakarta.transaction.Transactional;

public interface TeamService {

    void create(TeamDTO teamDTO) throws CustomException;

    void delete(long teamId) throws CustomException;

    TeamDTO find(Long teamId) throws CustomException;

    void update(TeamDTO teamDTO) throws CustomException;

    void addTeamMembersAndInvitesByTeam(Long teamId, TeamMemberAndInvite teamMemberAndInvite) throws Exception;

    void removeTeamMembersAndInvitesByTeam(Long teamId, TeamMemberAndInvite teamMemberAndInvite) throws Exception;
}
