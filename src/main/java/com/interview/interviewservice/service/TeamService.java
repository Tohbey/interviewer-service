package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.dtos.TeamMemberAndInvite;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;

public interface TeamService {

    void create(TeamDTO teamDTO) throws CustomException;

    void delete(Long teamId) throws CustomException;

    TeamDTO find(Long teamId) throws CustomException;

    void update(TeamDTO teamDTO) throws CustomException;

    void addTeamMembersAndInvitesByTeam(Long teamId, TeamMemberAndInvite teamMemberAndInvite) throws CustomException;

    void removeTeamMembersAndInvitesByTeam(Long teamId, TeamMemberAndInvite teamMemberAndInvite) throws CustomException;
}
