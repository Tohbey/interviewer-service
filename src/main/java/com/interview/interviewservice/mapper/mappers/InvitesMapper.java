package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.Invites;
import com.interview.interviewservice.mapper.DTOS.InvitesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvitesMapper {
    InvitesMapper INSTANCE = Mappers.getMapper(InvitesMapper.class);
    InvitesDTO inviteToInviteDTO(Invites invites);

    Invites inviteDTOToInvite(InvitesDTO invitesDTO);
}
