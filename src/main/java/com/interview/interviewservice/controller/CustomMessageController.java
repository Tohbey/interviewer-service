package com.interview.interviewservice.controller;


import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.CustomMessageDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.resource.BaseResource;
import com.interview.interviewservice.service.CustomMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(value = CustomMessageController.BASE_URL)
public class CustomMessageController {

    private final CustomMessageService customMessageService;


    public static final String BASE_URL= BaseResource.API+BaseResource.RELATIVEPATH+"custom-message"+BaseResource.RELATIVEPATH;

    public CustomMessageController(CustomMessageService customMessageService) {
        this.customMessageService = customMessageService;
    }

    @RequestMapping(method = RequestMethod.POST, value = BaseResource.SAVE)
    public IDataResponse createCustomMessage(@RequestBody CustomMessageDTO customMessageDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            customMessageService.create(customMessageDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Custom Message Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }

        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = BaseResource.FIND+BaseResource.RELATIVEPATH+"{customMessageId}")
    public IDataResponse findCustomMessage(@PathVariable("customMessageId") Long customMessageId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(Collections.singletonList(customMessageService.findCustomMessage(customMessageId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Custom Message Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }

        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "company"+BaseResource.RELATIVEPATH+BaseResource.FIND+BaseResource.RELATIVEPATH+"{companyId}")
    public IDataResponse findCustomMessage(@PathVariable("companyId") String companyId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(customMessageService.findCustomMessagesByCompanyId(companyId));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Custom Message Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }

        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = BaseResource.DELETE+BaseResource.RELATIVEPATH+"{customMessageId}")
    public IDataResponse deleteCustomMessage(@PathVariable("customMessageId") Long customMessageId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            customMessageService.delete(customMessageId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Custom Message Successfully Deleted","Deleted", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }

        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PUT, value = BaseResource.UPDATE)
    public IDataResponse updateCustomMessage(@RequestBody CustomMessageDTO customMessageDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            customMessageService.update(customMessageDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Custom Message Successfully Updated","Updated", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }

        return dataResponse;
    }

}
