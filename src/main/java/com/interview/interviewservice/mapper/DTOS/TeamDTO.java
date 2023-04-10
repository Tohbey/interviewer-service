package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TeamDTO extends BaseDTO {
    private String name;
    private String color;
    private String section;
    private String companyId;
//    private List<Userdto> teamMembers;
//
//    @Data
//    public static class Userdto{
//        private String surname;
//        private String otherNames;
//        private String fullname;
//        private String address;
//        private String phoneNumber;
//        private String email;
//        private Long id;
//        private boolean isUser;
//    }
}
