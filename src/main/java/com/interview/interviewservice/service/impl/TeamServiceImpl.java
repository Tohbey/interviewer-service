package com.interview.interviewservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.Util.KeyValuePair;
import com.interview.interviewservice.Util.ResultQuery;
import com.interview.interviewservice.dtos.ElasticSearchResponse;
import com.interview.interviewservice.dtos.TeamMemberAndInvite;
import com.interview.interviewservice.elastic.StageModel;
import com.interview.interviewservice.elastic.TeamModel;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Invites;
import com.interview.interviewservice.entity.Team;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.InvitesDTO;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.mapper.mappers.TeamMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.InvitesRepository;
import com.interview.interviewservice.repository.TeamRepository;
import com.interview.interviewservice.repository.UserRepository;
import com.interview.interviewservice.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    private final InvitesRepository invitesRepository;

    private final CompanyRepository companyRepository;

    private final InvitesService invitesService;

    private final UserContextService userContextService;

    @Autowired
    private ISearchService iSearchService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           TeamMapper teamMapper,
                           UserRepository userRepository,
                           UserService userService,
                           InvitesRepository invitesRepository, CompanyRepository companyRepository,
                           InvitesService invitesService,
                           UserContextService userContextService) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.invitesRepository = invitesRepository;
        this.companyRepository = companyRepository;
        this.invitesService = invitesService;
        this.userContextService = userContextService;
    }


    @Override
    @Transactional
    public void create(TeamDTO teamDTO) throws CustomException {
        validation(teamDTO);
        Team team = mapper(teamDTO);
        team.setFlag(Flag.ENABLED);
        Company company = companyRepository.findCompanyByCompanyId(teamDTO.getCompanyId());
        team.setCompany(company);


        team = teamRepository.save(team);

        if(!teamDTO.getInvites().isEmpty()){
            Team finalTeam = team;
            teamDTO.getInvites().forEach(invite -> {
                invite.setTeamId(finalTeam.getId());
                try {
                    invitesService.create(invite);
                } catch (CustomException e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }


        if(!teamDTO.getTeamMembers().isEmpty()){
            Team finalTeam = team;
            teamDTO.getTeamMembers().forEach(teamMember -> {
                Optional<User> user = userRepository.findById(teamMember.getId());
                if(user.isPresent()){
                    user.get().getTeams().add(finalTeam);
                    try {
                        userService.update(userService.mapper(user.get()));
                    } catch (CustomException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    @Transactional
    public void delete(Long teamId) throws CustomException {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            team.get().setFlag(Flag.DISABLED);
            team.get().setLastModifiedDate(new Date());
            team.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());

            List<Invites> invites = invitesRepository.findAllByTeam(team.get());
            if(!invites.isEmpty()) {
                invites.forEach(invite ->{
                    invite.setTeam(null);
                    invitesRepository.save(invite);
                });
            }

            List<User> users = userRepository.findUserByTeamsContains(team.get());
            if(!users.isEmpty()) {
                users.forEach(user -> {
                    user.getTeams().remove(team.get());

                    userRepository.save(user);
                });
            }

            teamRepository.save(team.get());
        }else{
            throw new CustomException("Team Not Found");
        }
    }

    @Override
    public TeamDTO find(Long teamId) throws CustomException {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            TeamDTO teamDTO = teamMapper.teamToTeamDTO(team.get());
            teamDTO.setInvites(invitesService.findInvitesByTeam(team.get()));
            teamDTO.setTeamMembers(userService.findUsersByTeam(team.get()));
            return teamDTO;
        }else{
            throw new CustomException("Team Not Found");
        }
    }

    @Override
    public void update(TeamDTO teamDTO) throws CustomException {
        Optional<Team> savedTeam = teamRepository.findById(teamDTO.getId());
        Company company = companyRepository.findCompanyByCompanyId(teamDTO.getCompanyId());

        if(savedTeam.isPresent() && Objects.nonNull(company)){
            validateUpdate(teamDTO, savedTeam.get());
            Team team = mapper(teamDTO);
            team.setCompany(company);

            teamRepository.save(team);
        }else{
            throw new CustomException("Team detail not found");
        }

    }

    @Override
    @Transactional
    public void addTeamMembersAndInvitesByTeam(Long teamId, TeamMemberAndInvite teamMemberAndInvite) throws CustomException {
        TeamDTO teamDTO = find(teamId);

        if(Objects.nonNull(teamDTO)){
            if(!teamMemberAndInvite.getInvites().isEmpty()){
                teamMemberAndInvite.getInvites().forEach(invite -> {
                    try {
                        InvitesDTO inviteDTO = invitesService.find(invite);
                        inviteDTO.setTeamId(teamId);
                        inviteDTO.setLastModifiedDate(new Date());
                        inviteDTO.setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
                        invitesService.update(inviteDTO);
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            if(!teamMemberAndInvite.getUserIds().isEmpty()){
                teamMemberAndInvite.getUserIds().forEach(userId  -> {
                    try {
                        UserDTO userDTO = userService.find(userId);
                        userDTO.getTeamDTO().add(teamDTO);
                        userDTO.setLastModifiedDate(new Date());
                        userDTO.setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
                        userService.update(userDTO);
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }

                });

            }
        }
    }

    @Override
    @Transactional
    public void removeTeamMembersAndInvitesByTeam(Long teamId, TeamMemberAndInvite teamMemberAndInvite) throws CustomException {
        Optional<Team> team = teamRepository.findById(teamId);

        if(team.isPresent()) {
            if(!teamMemberAndInvite.getInvites().isEmpty()){
                teamMemberAndInvite.getInvites().forEach(invite -> {
                    Optional<Invites> savedInvite = invitesRepository.findByIdAndTeam(invite, team.get());
                    if(savedInvite.isPresent()){
                        savedInvite.get().setTeam(null);
                        savedInvite.get().setLastModifiedDate(new Date());
                        try {
                            savedInvite.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
                        } catch (CustomException e) {
                            throw new RuntimeException(e);
                        }
                        invitesRepository.save(savedInvite.get());
                    }
                });
            }

            if(!teamMemberAndInvite.getUserIds().isEmpty()){
                teamMemberAndInvite.getUserIds().forEach(userId  -> {
                    Optional<User> savedUser = userRepository.findUserByIdAndTeams(userId, team.get());
                    if(savedUser.isPresent()){
                        savedUser.get().getTeams().remove(team.get());
                        savedUser.get().setLastModifiedDate(new Date());
                        try {
                            savedUser.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
                        } catch (CustomException e) {
                            throw new RuntimeException(e);
                        }
                        userRepository.save(savedUser.get());
                    }
                });

            }
        }else{
            throw new CustomException("Team Not Found");
        }
    }

    @Override
    public List<KeyValuePair> teamSearch(String query, String companyId) throws Exception {
        List<KeyValuePair> results = new ArrayList<>();
        String[] STAGE_FIELDS = {"name", "section"};
        ResultQuery resultQuery = iSearchService.searchFromQuery(query, STAGE_FIELDS, "team/", companyId);
        if(Objects.nonNull(resultQuery.getElements())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<ElasticSearchResponse<TeamModel>> elasticSearchResponses = objectMapper.readValue(resultQuery.getElements(),
                        new TypeReference<List<ElasticSearchResponse<TeamModel>>>() {
                        });

                if (!elasticSearchResponses.isEmpty()) {
                    for (ElasticSearchResponse elasticSearchResponse : elasticSearchResponses) {
                        TeamModel teamModel = (TeamModel) elasticSearchResponse.getSource();
                        KeyValuePair keyValuePair = new KeyValuePair(teamModel.getId(), teamModel.getName().concat(" (").concat(teamModel.getSection()).concat(")"));
                        results.add(keyValuePair);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }

        return results;
    }

    private void validation(TeamDTO teamDTO) throws CustomException{

        Company company = companyRepository.findCompanyByCompanyId(teamDTO.getCompanyId());
        if(Objects.isNull(company)){
            throw new CustomException("Company detail not found");
        }

        if(teamRepository.existsByNameAndCompany(teamDTO.getName(), company)){
            throw new CustomException("Team Name Already Exist");
        }

        if(StringUtils.isEmpty(teamDTO.getSection())){
            throw new CustomException("Section cant be empty");
        }
    }

    private void validateUpdate(TeamDTO teamDTO, Team savedTeam) throws CustomException {
        Company company = companyRepository.findCompanyByCompanyId(teamDTO.getCompanyId());

        if(Objects.isNull(company)){
            throw new CustomException("Company detail not found");
        }

        if(!savedTeam.getName().equalsIgnoreCase(teamDTO.getName())){
            if(teamRepository.existsByNameAndCompany(teamDTO.getName(), company)){
                throw new CustomException("Team Name Already Exist");
            }
        }

        if(!savedTeam.getSection().equalsIgnoreCase(teamDTO.getSection())){
            if(StringUtils.isEmpty(teamDTO.getSection())){
                throw new CustomException("Section cant be empty");
            }
        }
    }


    private Team mapper(TeamDTO teamDTO) throws CustomException {
        Team team = teamMapper.teamDTOToTeam(teamDTO);
        if(Objects.isNull(team.getId())){
            team.setCreatedDate(new Date());
            team.setCreatedBy(userContextService.getCurrentUserDTO().getFullname());
        }else{
            team.setLastModifiedDate(new Date());
            team.setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
        }

        return team;
    }
}
