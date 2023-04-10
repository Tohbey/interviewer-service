package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.InvitesDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.service.InvitesService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(InviteController.BASE_URL)
public class InviteController {

    public static final String BASE_URL="/api/v1/invite";

    private final InvitesService invitesService;


    public InviteController(InvitesService invitesService) {
        this.invitesService = invitesService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public IDataResponse createInvite(@RequestBody InvitesDTO invitesDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            invitesService.create(invitesDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Invite Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/find/{inviteId}")
    public IDataResponse findInvite(@PathVariable("inviteId") Long inviteId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(Collections.singletonList(invitesService.find(inviteId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Invite Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{inviteId}")
    public IDataResponse deleteInvite(@PathVariable("inviteId") Long inviteId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            invitesService.delete(inviteId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Invite Successfully Deleted","Deleted", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public IDataResponse updateInvite(@RequestBody InvitesDTO invitesDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            invitesService.update(invitesDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Invite Successfully Updated","Updated", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
