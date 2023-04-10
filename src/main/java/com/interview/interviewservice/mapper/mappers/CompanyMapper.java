package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.mapper.DTOS.CompanyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDTO companyToCompanyDTO(Company company);

    Company companyDTOToCompany(CompanyDTO companyDTO);
}
