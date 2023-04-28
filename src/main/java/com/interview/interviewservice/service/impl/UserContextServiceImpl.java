package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.config.CustomDetail;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.mapper.mappers.RoleMapper;
import com.interview.interviewservice.mapper.mappers.TeamMapper;
import com.interview.interviewservice.mapper.mappers.UserMapper;
import com.interview.interviewservice.repository.UserRepository;
import com.interview.interviewservice.service.UserContextService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserContextServiceImpl implements UserContextService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final TeamMapper teamMapper;


    public UserContextServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleMapper roleMapper, TeamMapper teamMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.teamMapper = teamMapper;
    }

    @Override
    public Optional<User> getCurrentUser() throws CustomException {
        CustomDetail userDetail = (CustomDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetail.getUsername();
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        return user;
    }

    @Override
    public UserDTO getCurrentUserDTO() throws CustomException {
        User user = getCurrentUser().get();
        UserDTO userDTO = userMapper.userToUserDTO(user);
        userDTO.setCompanyId(user.getCompany().getCompanyId());
        userDTO.setRole(roleMapper.roleToRoleDTO(user.getRole()));
        userDTO.setFullname(user.getUserFullName());
        TeamDTO teamDTO = teamMapper.teamToTeamDTO(user.getTeam());
        userDTO.setTeamDTO(teamDTO);

        return userDTO;
    }
}
