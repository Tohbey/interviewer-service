package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Team;
import com.interview.interviewservice.mapper.DTOS.CompanyDTO;
import com.interview.interviewservice.mapper.DTOS.RoleDTO;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.mapper.mappers.CompanyMapper;
import com.interview.interviewservice.mapper.mappers.TeamMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.model.RoleEnum;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.TeamRepository;
import com.interview.interviewservice.service.CompanyService;
import com.interview.interviewservice.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final UserService userService;

    private final CompanyMapper companyMapper;

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;


    public CompanyServiceImpl(CompanyRepository companyRepository, UserService userService, CompanyMapper companyMapper, TeamRepository teamRepository, TeamMapper teamMapper) {
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.companyMapper = companyMapper;
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    public void create(CompanyDTO companyDTO) throws CustomException {
        validate(companyDTO);
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        company.setCompanyId("#".concat(company.getCompanyId()).concat(company.getCompanyName().substring(0,2)));
        company.setFlag(Flag.ENABLED);

        company = companyRepository.save(company);

        String password = RandomStringUtils.randomAlphabetic(10);
        if(Objects.nonNull(company.getContactData())){
            RoleDTO roleDTO = new RoleDTO(1L, RoleEnum.ADMIN_USER);
            UserDTO userDTO = new UserDTO(
                    company.getContactData().getSurname(),
                    company.getContactData().getOthernames(),
                    company.getContactData().getSurname().concat(company.getContactData().getOthernames()),
                    company.getAddress(),
                    company.getContactData().getPhoneNumber(),
                    company.getContactData().getContactEmail(),
                    company.getPicture(),
                    company.getCompanyId(),
                    true,
                    password, roleDTO, new TeamDTO());

            userService.create(userDTO);
        }

    }

    @Override
    public void delete(Long companyId) throws Exception {
        Optional<Company> company = this.companyRepository.findById(companyId);
        if(company.isPresent()){
            company.get().setFlag(Flag.DISABLED);
            companyRepository.save(company.get());
        }else{
            throw new Exception("Company Not found");
        }
    }

    @Override
    public CompanyDTO find(Long companyId) throws CustomException {
        Optional<Company> company = this.companyRepository.findById(companyId);
        if(company.isPresent()){
            return companyMapper.companyToCompanyDTO(company.get());
        }else{
            throw new CustomException("Company Not Found");
        }
    }

    @Override
    public List<TeamDTO> findTeamsByCompany(Long companyId) throws CustomException {
        Optional<Company> company = this.companyRepository.findById(companyId);
        if(company.isPresent()){
            List<TeamDTO> teamDTOS = new ArrayList<>();
            List<Team> teams = this.teamRepository.findTeamsByCompany(company.get());

            teams.forEach(team -> {
                TeamDTO teamDTO = teamMapper.teamToTeamDTO(team);
                teamDTO.setCompanyId(company.get().getCompanyId());

                teamDTOS.add(teamDTO);
            });

            return teamDTOS;
        }else{
            throw new CustomException("Company Not found");
        }
    }

    @Override
    public void update(CompanyDTO companyDTO) throws CustomException {
        Optional<Company> savedCompany = companyRepository.findById(companyDTO.getId());
        if(savedCompany.isPresent()){
            validateUpdate(companyDTO, savedCompany.get());
            Company company = companyMapper.companyDTOToCompany(companyDTO);

            companyRepository.save(company);
        }else{
            throw new CustomException("Team detail not found");
        }
    }

    public void validate(CompanyDTO companyDTO) throws CustomException {
        if(Objects.isNull(companyDTO.getContactData())){
            throw new CustomException("Contact Info Can Not Be Empty");
        }

        if(companyRepository.existsByCompanyIdIgnoreCase(companyDTO.getCompanyId())){
            throw new CustomException("Company Already Exist");
        }

        if(companyRepository.existsByContactData_ContactEmailIgnoreCase(companyDTO.getContactData().getContactEmail())){
            throw new CustomException("Email Already Exist");
        }

        if(companyRepository.existsByContactData_PhoneNumber(companyDTO.getContactData().getPhoneNumber())){
            throw new CustomException("Phone Number Already Exist");
        }

    }

    private void validateUpdate(CompanyDTO companyDTO, Company savedCompany) throws CustomException {

        if(!savedCompany.getContactData().getContactEmail()
                .equalsIgnoreCase(companyDTO.getContactData().getContactEmail())){
            if(companyRepository.existsByContactData_ContactEmailIgnoreCase(companyDTO.getContactData().getContactEmail())){
                throw new CustomException("Email Already Exist");
            }
        }

        if(!savedCompany.getContactData().getPhoneNumber()
                .equalsIgnoreCase(companyDTO.getContactData().getPhoneNumber())){
            if(companyRepository.existsByContactData_PhoneNumber(companyDTO.getContactData().getPhoneNumber())){
                throw new CustomException("Phone Number Already Exist");
            }
        }
    }
}
