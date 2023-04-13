package com.interview.interviewservice.controller;


import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.JobDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.service.InterviewService;
import com.interview.interviewservice.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(value = JobController.BASE_URL)
public class JobController {

    public static final String BASE_URL="/api/v1/job";

    private final JobService jobService;

    private final InterviewService interviewService;

    public JobController(JobService jobService, InterviewService interviewService) {
        this.jobService = jobService;
        this.interviewService = interviewService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
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

    @RequestMapping(method = RequestMethod.GET, value = "/find/{jobId}")
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

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{jobId}")
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

    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public IDataResponse updateJob(@RequestBody JobDTO jobDTO){
        IDataResponse dataResponse = new IDataResponse();
        try{
            jobService.update(jobDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e){
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/interview/{jobId}")
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
}
