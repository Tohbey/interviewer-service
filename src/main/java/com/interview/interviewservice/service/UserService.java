package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.Util.ResultQuery;
import com.interview.interviewservice.entity.Team;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.model.Flag;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public interface UserService {

    void create(UserDTO userDTO) throws CustomException, MessagingException;

    void delete(Long userId) throws CustomException;

    UserDTO find(Long userId) throws CustomException;

    void update(UserDTO userDTO) throws CustomException;

    List<UserDTO> findUsersBy(String companyId, Flag flag) throws CustomException;

    UserDTO mapper(User user);

    List<UserDTO> findUsersByTeam(Team team);

    ResultQuery userSearch(String query, String companyId) throws IOException;
}
