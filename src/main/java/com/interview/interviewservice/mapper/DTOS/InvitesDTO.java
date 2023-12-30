package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitesDTO extends BaseDTO {
    private String surname;
    private String othernames;
    private String email;
    private Long teamId;
    private String teamName;
}
