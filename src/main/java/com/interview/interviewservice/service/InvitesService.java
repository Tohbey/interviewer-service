package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.mapper.DTOS.InvitesDTO;

public interface InvitesService {

    void create(InvitesDTO invitesDTO) throws CustomException;

    InvitesDTO find(Long inviteId) throws CustomException;

    void update(InvitesDTO invitesDTO) throws CustomException;

    void delete(Long inviteId) throws Exception;
}
