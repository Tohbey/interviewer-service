package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Invites;
import com.interview.interviewservice.entity.Team;
import com.interview.interviewservice.mapper.DTOS.InvitesDTO;
import com.interview.interviewservice.mapper.mappers.InvitesMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.InvitesRepository;
import com.interview.interviewservice.repository.TeamRepository;
import com.interview.interviewservice.service.InvitesService;
import com.interview.interviewservice.service.UserContextService;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class InviteServiceImpl implements InvitesService {

    private final InvitesRepository invitesRepository;

    private final InvitesMapper invitesMapper;

    private final TeamRepository teamRepository;

    private final UserContextService userContextService;

    public InviteServiceImpl(InvitesRepository invitesRepository,
                             InvitesMapper invitesMapper,
                             TeamRepository teamRepository,
                             UserContextService userContextService) {
        this.invitesRepository = invitesRepository;
        this.invitesMapper = invitesMapper;
        this.teamRepository = teamRepository;
        this.userContextService = userContextService;
    }

    @Override
    public void create(InvitesDTO invitesDTO) throws CustomException {
        validation(invitesDTO);
        Invites invite = invitesMapper.inviteDTOToInvite(invitesDTO);

        if(Objects.nonNull(invitesDTO.getTeamId())){
            Optional<Team> team = teamRepository.findById(invitesDTO.getTeamId());
            if(team.isPresent()){
                invite.setTeam(team.get());
            }else{
                throw new CustomException("Team Not found");
            }
        }
        invite.setCreatedDate(new Date());
        invite.setCreatedBy(userContextService.getCurrentUserDTO().getFullname());
        invite.setFlag(Flag.ENABLED);

        invitesRepository.save(invite);
    }

    @Override
    public InvitesDTO find(Long inviteId) throws CustomException {
        Optional<Invites> invite = invitesRepository.findById(inviteId);
        if(invite.isPresent()){
            InvitesDTO invitesDTO = invitesMapper.inviteToInviteDTO(invite.get());
            invitesDTO.setFlag(invite.get().getFlag());
            if(Objects.nonNull(invite.get().getTeam())){
                invitesDTO.setTeamName(invite.get().getTeam().getName());
                invitesDTO.setTeamId(invite.get().getTeam().getId());
            }
            return invitesDTO;
        }else{
            throw new CustomException("Invite Not found");
        }
    }

    @Override
    public void update(InvitesDTO invitesDTO) throws CustomException {
        Optional<Invites> savedInvite = invitesRepository.findById(invitesDTO.getId());
        if(savedInvite.isPresent()){
            validateUpdate(invitesDTO, savedInvite.get());
            Invites invite = invitesMapper.inviteDTOToInvite(invitesDTO);
            invite.setLastModifiedDate(new Date());
            if(Objects.nonNull(invitesDTO.getTeamId())){
                Optional<Team> team = teamRepository.findById(invitesDTO.getTeamId());
                team.ifPresent(invite::setTeam);
            }

            invite.setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());

            invitesRepository.save(invite);
        }else{
            throw new CustomException("Invite Doesnt Exist");
        }
    }

    @Override
    public void delete(Long inviteId) throws Exception {
        Optional<Invites> invite = invitesRepository.findById(inviteId);
        if(invite.isPresent()){
            invite.get().setFlag(Flag.DISABLED);
            invitesRepository.save(invite.get());
        }else{
            throw new Exception("Invite Not found");
        }
    }


    @Override
    public List<InvitesDTO> findInvitesByTeam(Team team) {
        List<InvitesDTO> invitesDTOS = new ArrayList<>();
        List<Invites> invites = invitesRepository.findAllByTeam(team);

        invites.forEach(invite -> {
            InvitesDTO invitesDTO = invitesMapper.inviteToInviteDTO(invite);
            invitesDTO.setTeamId(team.getId());
            invitesDTO.setTeamName(team.getName());
            invitesDTOS.add(invitesDTO);
        });

        return invitesDTOS;
    }

    private void validation(InvitesDTO invitesDTO) throws CustomException {
        if(invitesRepository.existsByEmail(invitesDTO.getEmail())){
            throw new CustomException("Email Already Exist");
        }

        if(Objects.nonNull(invitesDTO.getTeamId())){
            if(teamRepository.findById(invitesDTO.getTeamId()).isEmpty()){
                throw new CustomException("Team info not found");
            }

            if(Objects.nonNull(invitesDTO.getTeamId())){
                Optional<Team> team = teamRepository.findById(invitesDTO.getTeamId());
                if(team.isPresent()){
                    if(invitesRepository.existsByEmailAndTeam(invitesDTO.getEmail(), team.get())){
                        throw new CustomException("Invite has already been sent to this email");
                    }
                }else{
                    throw new CustomException("Team Info does not exist");
                }
            }
        }
    }

    private void validateUpdate(InvitesDTO invitesDTO, Invites savedInvite) throws CustomException {

        if(Objects.nonNull(savedInvite.getTeam()) && Objects.nonNull(invitesDTO.getTeamId())){
            if(!savedInvite.getTeam().getId().equals(invitesDTO.getTeamId())){
                if(Objects.nonNull(invitesDTO.getTeamId())){
                    if(teamRepository.findById(invitesDTO.getTeamId()).isEmpty()){
                        throw new CustomException("Team info not found");
                    }
                }
            }
        }

        if(!savedInvite.getEmail().equalsIgnoreCase(invitesDTO.getEmail())){
            if(Objects.nonNull(invitesDTO.getEmail())){
                if(invitesRepository.existsByEmail(invitesDTO.getEmail())){
                    throw new CustomException("Email Already Exist");
                }

                if(invitesRepository.existsByEmail(invitesDTO.getEmail())){
                    throw new CustomException("Email Already Exist");
                }

                if(Objects.nonNull(invitesDTO.getTeamId())){
                    Optional<Team> team = teamRepository.findById(invitesDTO.getTeamId());
                    if(team.isPresent()){
                        if(invitesRepository.existsByEmailAndTeam(invitesDTO.getEmail(), team.get())){
                            throw new CustomException("Invite has already been sent to this email");
                        }
                    }else{
                        throw new CustomException("Team Info does not exist");
                    }
                }
            }
        }
    }
}
