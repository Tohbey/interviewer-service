package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.JobApplicationDTO;
import com.interview.interviewservice.model.ApplicationStatus;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.resource.BaseResource;
import com.interview.interviewservice.service.JobApplicationService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//  Testing Completed.
@RestController
@RequestMapping(value = JobApplicationController.BASE_URL)
public class JobApplicationController {

    public static final String BASE_URL= BaseResource.API+BaseResource.RELATIVEPATH+"application"+BaseResource.RELATIVEPATH;

    private final JobApplicationService jobApplicationService;


    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.POST, value = BaseResource.SAVE)
    public IDataResponse createApplication(@RequestBody JobApplicationDTO jobApplicationDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            jobApplicationService.create(jobApplicationDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.GET, value = BaseResource.FIND+BaseResource.RELATIVEPATH+"{jobApplicationId}")
    public IDataResponse findApplication(@PathVariable("jobApplicationId") Long jobApplicationId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(Collections.singletonList(jobApplicationService.find(jobApplicationId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Retrieved","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = BaseResource.DELETE+BaseResource.RELATIVEPATH+"{jobApplicationId}")
    public IDataResponse deleteApplication(@PathVariable("jobApplicationId") Long jobApplicationId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            jobApplicationService.delete(jobApplicationId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Deleted","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.GET, value = "company"+BaseResource.RELATIVEPATH+"{companyId}")
    public IDataResponse JobApplicationsByCompany(@PathVariable("companyId") String companyId, @RequestParam(value = "status", required = false) ApplicationStatus status){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(jobApplicationService.jobApplicationByCompany(companyId, status));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Retrieved","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.GET, value = "job"+BaseResource.RELATIVEPATH+"{jobId}")
    public IDataResponse JobApplicationsByJob(@PathVariable("jobId") Long jobId, @RequestParam(value = "status", required = false) ApplicationStatus status){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(jobApplicationService.jobApplicationByJob(jobId, status));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Retrieved","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.GET, value = "candidate"+BaseResource.RELATIVEPATH+"{candidateId}")
    public IDataResponse JobApplicationsByCandidate(@PathVariable("candidateId") Long candidateId, @RequestParam(value = "status", required = false) ApplicationStatus status){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(jobApplicationService.jobApplicationByCandidate(candidateId, status));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Retrieved","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.PUT, value = "approve")
    public IDataResponse approveJobApplications(@RequestBody @NotEmpty List<Long> ids, @RequestParam("comment") String comment){
        IDataResponse dataResponse = new IDataResponse();
        try {
            jobApplicationService.approveJobApplications(ids, comment);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Approved","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.PUT, value = "reject")
    public IDataResponse rejectJobApplications(@RequestBody @NotEmpty List<Long> ids, @RequestParam("comment") String comment){
        IDataResponse dataResponse = new IDataResponse();
        try {
            jobApplicationService.rejectJobApplications(ids, comment);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Reject","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.PUT, value = "approve"+BaseResource.RELATIVEPATH+"{jobApplicationId}")
    public IDataResponse approveJobApplication(@PathVariable("jobApplicationId") Long id, @RequestParam("comment") String comment){
        IDataResponse dataResponse = new IDataResponse();
        try {
            List<Long> ids = new ArrayList<Long>();
            ids.add(id);
            jobApplicationService.approveJobApplications(ids, comment);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Approved","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Test Completed✔
    @RequestMapping(method = RequestMethod.PUT, value = "reject"+BaseResource.RELATIVEPATH+"{jobApplicationId}")
    public IDataResponse rejectJobApplication(@PathVariable("jobApplicationId") Long id, @RequestParam("comment") String comment){
        IDataResponse dataResponse = new IDataResponse();
        try {
            List<Long> ids = new ArrayList<Long>();
            ids.add(id);
            jobApplicationService.rejectJobApplications(ids, comment);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Job Application Successfully Rejected","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
