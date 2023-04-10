package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}
