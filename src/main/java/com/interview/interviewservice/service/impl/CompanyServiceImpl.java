package com.interview.interviewservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.Util.KeyValuePair;
import com.interview.interviewservice.Util.ResultQuery;
import com.interview.interviewservice.dtos.ElasticSearchResponse;
import com.interview.interviewservice.elastic.CompanyModel;
import com.interview.interviewservice.elastic.UserModel;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Team;
import com.interview.interviewservice.mapper.DTOS.*;
import com.interview.interviewservice.mapper.mappers.CompanyMapper;
import com.interview.interviewservice.mapper.mappers.TeamMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.model.RoleEnum;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.TeamRepository;
import com.interview.interviewservice.service.*;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final UserService userService;

    private final CompanyMapper companyMapper;

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    @Autowired
    private ISearchService iSearchService;


    private final UserContextService userContextService;

    private final InvitesService invitesService;


    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl .class);

    public CompanyServiceImpl(CompanyRepository companyRepository,
                              UserService userService,
                              CompanyMapper companyMapper,
                              TeamRepository teamRepository,
                              TeamMapper teamMapper,
                              UserContextService userContextService, InvitesService invitesService) {
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.companyMapper = companyMapper;
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.userContextService = userContextService;
        this.invitesService = invitesService;
    }

    @Override
    @Transactional
    public void create(CompanyDTO companyDTO) throws CustomException, MessagingException {
        validate(companyDTO);
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        company.setCompanyId(company.getCompanyId().concat(company.getCompanyName().substring(0,2)));
        company.setFlag(Flag.ENABLED);

        company = companyRepository.save(company);


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
                    "", roleDTO, null, false);

            userService.create(userDTO);
        }

    }

    @Override
    public void delete(Long companyId) throws Exception {
        Optional<Company> company = this.companyRepository.findById(companyId);
        if(company.isPresent()){
            company.get().setFlag(Flag.DISABLED);
            company.get().setLastModifiedDate(new Date());
            company.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
            companyRepository.save(company.get());
        }else{
            throw new Exception("Company Not found");
        }
    }

    @Override
    public CompanyDTO find(String companyId) throws CustomException {
        Company company = this.companyRepository.findCompanyByCompanyId(companyId);
        if(Objects.nonNull(company)){
            return companyMapper.companyToCompanyDTO(company);
        }else{
            throw new CustomException("Company Not Found");
        }
    }

    @Override
    public List<TeamDTO> findTeamsByCompany(String companyId) throws CustomException {
        Company company = this.companyRepository.findCompanyByCompanyId(companyId);

        if(Objects.nonNull(company)){
            List<TeamDTO> teamDTOS = new ArrayList<>();
            List<Team> teams = this.teamRepository.findTeamsByCompany(company);

            teams.forEach(team -> {
                TeamDTO teamDTO = teamMapper.teamToTeamDTO(team);
                teamDTO.setCompanyId(company.getCompanyId());
                teamDTO.setInvites(invitesService.findInvitesByTeam(team));
                teamDTO.setTeamMembers(userService.findUsersByTeam(team));
                teamDTOS.add(teamDTO);
            });

            return teamDTOS;
        }else{
            throw new CustomException("Company Info Not found");
        }
    }

    @Override
    public List<KeyValuePair> companySearch(String query) throws Exception {
        List<KeyValuePair> results = new ArrayList<>();
        String[] COMPANY_FIELDS = {"companyName", "country"};
        ResultQuery resultQuery = iSearchService.searchFromQuery(query, COMPANY_FIELDS, "company/", "");
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            List<ElasticSearchResponse<CompanyModel>> elasticSearchResponses = objectMapper.readValue(resultQuery.getElements(),
                    new TypeReference<List<ElasticSearchResponse<CompanyModel>>>() {});

            if(!elasticSearchResponses.isEmpty()){
                for(ElasticSearchResponse elasticSearchResponse: elasticSearchResponses){
                    CompanyModel companyModel = (CompanyModel) elasticSearchResponse.getSource();
                    KeyValuePair keyValuePair = new KeyValuePair(companyModel.getId(), companyModel.getCompanyName()+" ("+companyModel.getCountry()+")");
                    results.add(keyValuePair);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        return results;
    }

    @Override
    public void update(CompanyDTO companyDTO) throws CustomException {
        Optional<Company> savedCompany = companyRepository.findById(companyDTO.getId());
        if(savedCompany.isPresent()){
            validateUpdate(companyDTO, savedCompany.get());
            Company company = mapper(companyDTO);

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

    private Company mapper(CompanyDTO companyDTO) throws CustomException {
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        if(Objects.nonNull(company.getId())){
            company.setLastModifiedDate(new Date());
            company.setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
        }

        return company;
    }
}
