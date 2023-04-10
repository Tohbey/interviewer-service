package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.Team;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    TeamDTO teamToTeamDTO(Team team);

    Team teamDTOToTeam(TeamDTO teamDTO);
}
