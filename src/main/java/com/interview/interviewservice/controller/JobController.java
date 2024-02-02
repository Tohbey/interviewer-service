package com.interview.interviewservice.controller;


import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.JobDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.resource.BaseResource;
import com.interview.interviewservice.service.InterviewService;
import com.interview.interviewservice.service.JobService;
import com.interview.interviewservice.service.JobTicketService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(value = JobController.BASE_URL)
public class JobController {

    public static final String BASE_URL=  BaseResource.API+BaseResource.RELATIVEPATH+"job"+BaseResource.RELATIVEPATH;

    private final JobService jobService;

    private final InterviewService interviewService;

    private final JobTicketService jobTicketService;

    public JobController(JobService jobService, InterviewService interviewService, JobTicketService jobTicketService) {
        this.jobService = jobService;
        this.interviewService = interviewService;
        this.jobTicketService = jobTicketService;
    }

//  Test Completed✔
    @RequestMapping(method = RequestMethod.POST, value = BaseResource.SAVE)
    public IDataResponse createJob(@RequestBody JobDTO jobDTO){
        IDataResponse dataResponse = new IDataResponse();
        try{
            jobService.create(jobDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.GET, value = BaseResource.FIND+BaseResource.RELATIVEPATH+"{jobId}")
    public IDataResponse findJob(@PathVariable("jobId") Long jobId){
        IDataResponse dataResponse = new IDataResponse();
        try{
            dataResponse.setData(Collections.singletonList(jobService.find(jobId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.DELETE, value = BaseResource.DELETE+BaseResource.RELATIVEPATH+"{jobId}")
    public IDataResponse deleteJob(@PathVariable("jobId") Long jobId){
        IDataResponse dataResponse = new IDataResponse();
        try{
            jobService.delete(jobId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Successfully Deleted","Deleted", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.PUT, value = BaseResource.UPDATE)
    public IDataResponse updateJob(@RequestBody JobDTO jobDTO){
        IDataResponse dataResponse = new IDataResponse();
        try{
            jobService.update(jobDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Successfully Updated","Updated", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "interview/{jobId}")
    public IDataResponse getInterviewsByJob(@PathVariable ("jobId") Long jobId){
        IDataResponse dataResponse = new IDataResponse();
        try{
            dataResponse.setData(interviewService.findInterviewsByJob(jobId));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Interviews Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }


    @RequestMapping(method = RequestMethod.GET, value = "job-ticket/{jobId}")
    public IDataResponse getTicketsByJob(@PathVariable ("jobId") Long jobId){
        IDataResponse dataResponse = new IDataResponse();
        try{
            dataResponse.setData(jobTicketService.jobTicketsByJob(jobId));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Interviews Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = BaseResource.SEARCH+BaseResource.RELATIVEPATH+"{companyId}")
    public IDataResponse search(@RequestParam("query") String query, @PathVariable("companyId") String companyId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(Collections.singletonList(jobService.jobSearch(query, companyId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("User Successfully Updated","Deleted", Message.Severity.SUCCESS));
        }catch (Exception e) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
