package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Team;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.mapper.DTOS.UserDTO;

import java.util.List;

public interface TeamService {

    Team create(TeamDTO teamDTO) throws CustomException;

    void delete(long teamId) throws CustomException;

    TeamDTO find(Long teamId) throws CustomException;

    void update(TeamDTO teamDTO) throws CustomException;
}
