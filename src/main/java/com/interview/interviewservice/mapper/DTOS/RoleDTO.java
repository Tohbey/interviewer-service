package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import com.interview.interviewservice.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private RoleEnum roleName;
}
