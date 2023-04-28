package com.interview.interviewservice.dtos;


import lombok.Data;

import java.util.List;

@Data
public class TeamMemberAndInvite {
    private List<Long> userIds;
    private List<Long> invites;
}
