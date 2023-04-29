package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.InvitesDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.resource.BaseResource;
import com.interview.interviewservice.service.InvitesService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

//  Testing Completed.
@RestController
@RequestMapping(InviteController.BASE_URL)
public class InviteController {

    public static final String BASE_URL= BaseResource.API+BaseResource.RELATIVEPATH+"invite"+BaseResource.RELATIVEPATH;

    private final InvitesService invitesService;


    public InviteController(InvitesService invitesService) {
        this.invitesService = invitesService;
    }

    @RequestMapping(method = RequestMethod.POST, value = BaseResource.SAVE)
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


    @RequestMapping(method = RequestMethod.GET, value = BaseResource.FIND+BaseResource.RELATIVEPATH+"{inviteId}")
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

    @RequestMapping(method = RequestMethod.DELETE, value = BaseResource.DELETE+BaseResource.RELATIVEPATH+"{inviteId}")
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

    @RequestMapping(method = RequestMethod.PUT, value = BaseResource.UPDATE)
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
