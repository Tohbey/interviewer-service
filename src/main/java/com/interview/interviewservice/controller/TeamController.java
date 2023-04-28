package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.dtos.TeamMemberAndInvite;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.resource.BaseResource;
import com.interview.interviewservice.service.InterviewService;
import com.interview.interviewservice.service.InvitesService;
import com.interview.interviewservice.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(TeamController.BASE_URL)
public class TeamController {

    public static final String BASE_URL= BaseResource.API+BaseResource.RELATIVEPATH+"team"+BaseResource.RELATIVEPATH;

    private final TeamService teamService;

    private final InvitesService invitesService;

    private final InterviewService interviewService;

    public TeamController(TeamService teamService, InvitesService invitesService, InterviewService interviewService) {
        this.teamService = teamService;
        this.invitesService = invitesService;
        this.interviewService = interviewService;
    }

    @RequestMapping(method = RequestMethod.POST, value = BaseResource.SAVE)
    public IDataResponse createTeam(@RequestBody TeamDTO teamDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            teamService.create(teamDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Team Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }


    @RequestMapping(method = RequestMethod.GET, value = BaseResource.FIND+BaseResource.RELATIVEPATH+"{teamId}")
    public IDataResponse<TeamDTO> findTeam(@PathVariable("teamId") Long teamId){
        IDataResponse<TeamDTO> dataResponse = new IDataResponse<TeamDTO>();
        try {
            dataResponse.setData(Collections.singletonList(teamService.find(teamId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Team Successfully Retrieved","Retrieved", Message.Severity.SUCCESS));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = BaseResource.DELETE+BaseResource.RELATIVEPATH+"{teamId}")
    public IDataResponse deleteTeam(@PathVariable("teamId") Long teamId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            teamService.delete(teamId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Team Successfully Deleted","Deleted", Message.Severity.SUCCESS));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PUT, value = BaseResource.UPDATE)
    public IDataResponse updateTeam(@RequestBody TeamDTO teamDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            teamService.update(teamDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Team Successfully Deleted","Deleted", Message.Severity.SUCCESS));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "add/team-members/{teamId}")
    public IDataResponse addTeamMembersAndInvitesByTeam(@PathVariable("teamId") Long teamId, @RequestBody TeamMemberAndInvite teamMemberAndInvite){
        IDataResponse dataResponse = new IDataResponse();
        try {
            teamService.addTeamMembersAndInvitesByTeam(teamId, teamMemberAndInvite);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Invites Successfully Retrieved","Retrieved", Message.Severity.SUCCESS));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }


    @RequestMapping(method = RequestMethod.GET, value = "interview/{teamId}")
    public IDataResponse getInterviewsByTeam(@PathVariable ("teamId") Long teamId){
        IDataResponse dataResponse = new IDataResponse();
        try{
            dataResponse.setData(interviewService.findInterviewsByTeam(teamId));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Interviews Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
