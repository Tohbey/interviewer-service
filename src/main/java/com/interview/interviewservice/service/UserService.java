package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.model.Flag;

import java.util.List;

public interface UserService {

    void create(UserDTO userDTO) throws CustomException;

    void delete(Long userId) throws Exception;

    UserDTO find(Long userId) throws CustomException;

    void update(UserDTO userDTO) throws CustomException;

    List<UserDTO> findUsersBy(Long companyId, Flag flag) throws CustomException;

    UserDTO mapper(User user);
}
