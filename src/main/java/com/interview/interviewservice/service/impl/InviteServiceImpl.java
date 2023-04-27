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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class InviteServiceImpl implements InvitesService {

    private final InvitesRepository invitesRepository;

    private final InvitesMapper invitesMapper;

    private final TeamRepository teamRepository;

    public InviteServiceImpl(InvitesRepository invitesRepository, InvitesMapper invitesMapper, TeamRepository teamRepository) {
        this.invitesRepository = invitesRepository;
        this.invitesMapper = invitesMapper;
        this.teamRepository = teamRepository;
    }

    @Override
    public void create(InvitesDTO invitesDTO) throws CustomException {
        validation(invitesDTO);
        Invites invite = invitesMapper.inviteDTOToInvite(invitesDTO);

        if(Objects.nonNull(invite.getTeam())){
            Optional<Team> team = teamRepository.findById(invitesDTO.getTeamId());
            if(team.isPresent()){
                invite.setTeam(team.get());
                invite.setFlag(Flag.ENABLED);

                invitesRepository.save(invite);
            }else{
                throw new CustomException("Team Not found");
            }
        }else{
            throw new CustomException("Team Id cant be empty");
        }

    }

    @Override
    public InvitesDTO find(Long inviteId) throws CustomException {
        Optional<Invites> invite = invitesRepository.findById(inviteId);
        if(invite.isPresent()){
            return invitesMapper.inviteToInviteDTO(invite.get());
        }else{
            throw new CustomException("Invite Not found");
        }
    }

    @Override
    public void update(InvitesDTO invitesDTO) {
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
    public List<InvitesDTO> findInvitesByTeam(Long teamId) throws Exception {
        List<InvitesDTO> invitesDTOS = new ArrayList<>();
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            List<Invites> invites = invitesRepository.findAllByTeam(team.get());
            invites.forEach(invite -> {
                InvitesDTO invitesDTO = invitesMapper.inviteToInviteDTO(invite);
                invitesDTO.setTeamId(team.get().getId());
                invitesDTOS.add(invitesDTO);
            });

            return invitesDTOS;
        }else{
            throw new Exception("Invite Not found");
        }
    }

    private void validation(InvitesDTO invitesDTO) throws CustomException {
        if(Objects.isNull(invitesDTO.getTeamId())){
            throw new CustomException("Team cant be empty");
        }

        if(invitesRepository.existsByEmailAndTeam(invitesDTO.getEmail(), invitesDTO.getTeamId())){
            throw new CustomException("Invite has already been sent to this email");
        }

        if(invitesRepository.existsByEmail(invitesDTO.getEmail())){
            throw new CustomException("Email Already Exist");
        }

        if(teamRepository.findById(invitesDTO.getTeamId()).isEmpty()){
            throw new CustomException("Team info not found");
        }
    }
}
