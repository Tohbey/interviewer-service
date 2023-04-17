package com.interview.interviewservice.controller;


import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.InterviewDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.resource.BaseResource;
import com.interview.interviewservice.service.InterviewService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(value = InterviewController.BASE_URL)
public class InterviewController {

    public static final String BASE_URL= BaseResource.API+BaseResource.RELATIVEPATH+"interview"+BaseResource.RELATIVEPATH;

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @RequestMapping(method = RequestMethod.POST, value = BaseResource.SAVE)
    public IDataResponse createInterview(@RequestBody InterviewDTO interviewDTO){
        IDataResponse dataResponse = new IDataResponse();
        try{
            interviewService.create(interviewDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Interview Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = BaseResource.FIND+BaseResource.RELATIVEPATH+"{interviewId}")
    public IDataResponse findInterview(@PathVariable("interviewId") Long interviewId){
        IDataResponse dataResponse = new IDataResponse();
        try{
            dataResponse.setData(Collections.singletonList(interviewService.findInterview(interviewId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Interview Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = BaseResource.DELETE+BaseResource.RELATIVEPATH+"{interviewId}")
    public IDataResponse deleteInterview(@PathVariable("interviewId") Long interviewId){
        IDataResponse dataResponse = new IDataResponse();
        try{
            interviewService.delete(interviewId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Interview Successfully Deleted","Deleted", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PUT, value = BaseResource.UPDATE)
    public IDataResponse updateInterview(@RequestBody InterviewDTO interviewDTO){
        IDataResponse dataResponse = new IDataResponse();
        try{
            interviewService.update(interviewDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Interview Successfully Updated","Updated", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
