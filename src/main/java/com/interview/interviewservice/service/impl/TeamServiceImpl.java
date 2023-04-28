package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.dtos.TeamMemberAndInvite;
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
import com.interview.interviewservice.repository.TeamRepository;
import com.interview.interviewservice.repository.UserRepository;
import com.interview.interviewservice.service.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    private final CompanyRepository companyRepository;

    private final InvitesService invitesService;

    private final UserContextService userContextService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           TeamMapper teamMapper,
                           UserRepository userRepository,
                           UserService userService,
                           CompanyRepository companyRepository,
                           InvitesService invitesService,
                           UserContextService userContextService) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.companyRepository = companyRepository;
        this.invitesService = invitesService;
        this.userContextService = userContextService;
    }


    @Override
    public void create(TeamDTO teamDTO) throws CustomException {
        validation(teamDTO);
        Team team = mapper(teamDTO);
        team.setFlag(Flag.ENABLED);

        team = teamRepository.save(team);

        if(teamDTO.getInvites().size() > 0){
            Team finalTeam = team;
            teamDTO.getInvites().forEach(invite -> {
                InvitesDTO invitesDTO = new InvitesDTO(invite.getSurname(), invite.getOthernames(), invite.getEmail(), finalTeam.getId());
                try {
                    invitesService.create(invitesDTO);
                } catch (CustomException e) {
                    throw new RuntimeException(e);
                }
            });
        }


        if(teamDTO.getTeamMembers().size() > 0){
            Team finalTeam = team;
            teamDTO.getTeamMembers().forEach(teamMember -> {
                Optional<User> user = userRepository.findById(teamMember.getId());
                if(user.isPresent()){
                    user.get().setTeam(finalTeam);
                    try {
                        userService.update(userService.mapper(user.get()));
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    @Override
    public void delete(long teamId) throws CustomException {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            team.get().setFlag(Flag.DISABLED);
            team.get().setLastModifiedDate(new Date());
            team.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());

            teamRepository.save(team.get());
        }else{
            throw new CustomException("Team Not Found");
        }
    }

    @Override
    public TeamDTO find(Long teamId) throws CustomException {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            return teamMapper.teamToTeamDTO(team.get());
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
    public void addTeamMembersAndInvitesByTeam(Long teamId, TeamMemberAndInvite teamMemberAndInvite) throws Exception {
        TeamDTO teamDTO = find(teamId);

        if(Objects.nonNull(teamDTO)){
            if(teamMemberAndInvite.getInvites().size() > 0){
                teamMemberAndInvite.getInvites().forEach(invite -> {
                    try {
                        InvitesDTO inviteDTO = invitesService.find(invite);
                        inviteDTO.setTeamId(teamId);
                        invitesService.update(inviteDTO);
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            if(teamMemberAndInvite.getUserIds().size() > 0){
                teamMemberAndInvite.getUserIds().forEach(userId  -> {
                    try {
                        UserDTO userDTO = userService.find(userId);
                        userDTO.setTeamDTO(teamDTO);
                        userService.update(userDTO);
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }

                });

            }
        }
    }

    private void validation(TeamDTO teamDTO) throws CustomException{

        Company company = companyRepository.findCompanyByCompanyId(teamDTO.getCompanyId());
        if(Objects.isNull(company)){
            throw new CustomException("Company detail not found");
        }

        if(teamRepository.existsByNameAndCompany(teamDTO.getName(), company)){
            throw new CustomException("Team Name Already Exist");
        }

        if(teamRepository.existsBySectionAndCompany(teamDTO.getSection(), company)){
            throw new CustomException("Section Already Exist");
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
            if(teamRepository.existsBySectionAndCompany(teamDTO.getSection(), company)){
                throw new CustomException("Section Already Exist");
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
