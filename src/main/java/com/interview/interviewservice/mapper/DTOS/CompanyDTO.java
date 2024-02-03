package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import lombok.Data;

@Data
public class CompanyDTO extends BaseDTO {
    private String companyId;
    private String picture;
    private String address;
    private Long countryId;
    private String country;
    private String companyName;
    private ContactData contactData;

    @Data
    public static class ContactData{
        private String contactEmail;
        private String phoneNumber;
        private String surname;
        private String othernames;
    }
}
