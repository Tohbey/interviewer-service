package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.JobTicketDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.resource.BaseResource;
import com.interview.interviewservice.service.JobTicketService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(JobTicketController.BASE_URL)
public class JobTicketController {

    public  static final String BASE_URL =  BaseResource.API+BaseResource.RELATIVEPATH+"job-ticket"+BaseResource.RELATIVEPATH;

    private final JobTicketService jobTicketService;

    public JobTicketController(JobTicketService jobTicketService) {
        this.jobTicketService = jobTicketService;
    }

    @RequestMapping(method = RequestMethod.POST, value = BaseResource.SAVE)
    public IDataResponse createJobTicket(@RequestBody JobTicketDTO jobTicketDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            jobTicketService.create(jobTicketDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Ticket Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = BaseResource.FIND+BaseResource.RELATIVEPATH+"{jobTicketId}")
    public IDataResponse findJobTicket(@PathVariable("jobTicketId") Long jobTicketId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(Collections.singletonList(jobTicketService.find(jobTicketId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Ticket Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = BaseResource.DELETE+BaseResource.RELATIVEPATH+"{jobTicketId}")
    public IDataResponse deleteJobTicket(@PathVariable("jobTicketId") Long jobTicketId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            jobTicketService.delete(jobTicketId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Ticket Successfully Deleted","Deleted", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PUT, value = BaseResource.UPDATE)
    public IDataResponse updateJobTicket(@RequestBody JobTicketDTO jobTicketDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            jobTicketService.update(jobTicketDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Ticket Successfully Updated","Updated", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
