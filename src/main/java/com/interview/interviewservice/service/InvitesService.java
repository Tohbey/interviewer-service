package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Invites;
import com.interview.interviewservice.mapper.DTOS.InvitesDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface InvitesService {

    void create(InvitesDTO invitesDTO) throws CustomException;

    InvitesDTO find(Long inviteId) throws CustomException;

    void update(InvitesDTO invitesDTO);

    void delete(Long inviteId) throws Exception;

    List<InvitesDTO> findInvitesByTeam(Long teamId) throws Exception;
}
