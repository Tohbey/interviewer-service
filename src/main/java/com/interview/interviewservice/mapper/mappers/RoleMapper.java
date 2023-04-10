package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.Role;
import com.interview.interviewservice.mapper.DTOS.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO roleToRoleDTO(Role role);

    Role roleDTOToRole(RoleDTO roleDTO);

}
