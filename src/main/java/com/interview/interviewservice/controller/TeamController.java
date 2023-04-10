package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.InvitesDTO;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.service.InvitesService;
import com.interview.interviewservice.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(TeamController.BASE_URL)
public class TeamController {

    public static final String BASE_URL="/api/v1/team";

    private final TeamService teamService;

    private final InvitesService invitesService;


    public TeamController(TeamService teamService, InvitesService invitesService) {
        this.teamService = teamService;
        this.invitesService = invitesService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
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


    @RequestMapping(method = RequestMethod.GET, value = "/find/{teamId}")
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

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{teamId}")
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

    @RequestMapping(method = RequestMethod.PUT, value = "/update")
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

    @RequestMapping(method = RequestMethod.GET, value = "/invites/{teamId}")
    public IDataResponse<InvitesDTO> findInvitesByTeam(@PathVariable("teamId") Long teamId){
        IDataResponse<InvitesDTO> dataResponse = new IDataResponse<InvitesDTO>();
        try {
            dataResponse.setData(invitesService.findInvitesByTeam(teamId));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Invites Successfully Retrieved","Retrieved", Message.Severity.SUCCESS));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
