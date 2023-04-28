package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.UserDTO;

import java.util.Optional;

public interface UserContextService {

    Optional<User> getCurrentUser() throws CustomException;

    UserDTO getCurrentUserDTO() throws CustomException;

}
