package com.interview.interviewservice.mapper.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitesDTO {
    private String surname;
    private String othernames;
    private String email;
    private Long teamId;
}
