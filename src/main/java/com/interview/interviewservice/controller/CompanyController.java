package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.*;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.service.CompanyService;
import com.interview.interviewservice.service.JobService;
import com.interview.interviewservice.service.StageService;
import com.interview.interviewservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(CompanyController.BASE_URL)
public class CompanyController {

    public static final String BASE_URL="/api/v1/comp";

    private final CompanyService companyService;

    private final UserService userService;

    private final StageService stageService;

    private final JobService jobService;

    public CompanyController(CompanyService companyService, UserService userService, StageService stageService, JobService jobService) {
        this.companyService = companyService;
        this.userService = userService;
        this.stageService = stageService;
        this.jobService = jobService;
    }

    //  Testing Completed.
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public IDataResponse createCompany(@RequestBody CompanyDTO companyDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            companyService.create(companyDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Corporate Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Testing Completed.
    @RequestMapping(method = RequestMethod.GET, value = "/find/{companyId}")
    public IDataResponse<CompanyDTO> findCompany(@PathVariable("companyId") Long companyId){
        IDataResponse<CompanyDTO> dataResponse = new IDataResponse<CompanyDTO>();
        try {
            dataResponse.setData(Collections.singletonList(companyService.find(companyId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Corporate Successfully Retrieved","Retrieved", Message.Severity.SUCCESS));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Testing Completed.
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{companyId}")
    public IDataResponse deleteCompany(@PathVariable("companyId") Long companyId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            companyService.delete(companyId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Corporate Successfully Deleted","Deleted", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }


    //  Testing Completed.
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public IDataResponse updateCompany(@RequestBody CompanyDTO companyDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            companyService.update(companyDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Corporate Successfully Updated","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/teams/{companyId}")
    public IDataResponse<TeamDTO> findTeamsByCompany(@PathVariable("companyId") Long companyId){
        IDataResponse<TeamDTO> dataResponse = new IDataResponse<TeamDTO>();
        try {
            dataResponse.setData(this.companyService.findTeamsByCompany(companyId));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Teams Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Testing Completed.
    @RequestMapping(method = RequestMethod.GET, value = "/users/{companyId}/{flag}")
    public IDataResponse<UserDTO> findUsersByCompanyAndFlag(@PathVariable("companyId") Long companyId, @PathVariable("flag") Flag flag){
        IDataResponse<UserDTO> dataResponse = new IDataResponse<UserDTO>();
        try {
            dataResponse.setData(this.userService.findUsersBy(companyId, flag));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Teams Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    //  Testing Completed.
    @RequestMapping(method = RequestMethod.GET, value = "/stages/{companyId}/{flag}")
    public IDataResponse<StageDTO> findStagesByCompany(@PathVariable("companyId") Long companyId, @PathVariable("flag") Flag flag){
        IDataResponse<StageDTO> dataResponse = new IDataResponse<StageDTO>();
        try {
            dataResponse.setData(stageService.findStagesByCompany(companyId, flag));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Stages Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/job/{companyId}/{flag}")
    public IDataResponse<JobDTO> findJobsByCompanyAndFlag(@PathVariable("companyId") Long companyId, @PathVariable("flag") Flag flag){
        IDataResponse<JobDTO> dataResponse = new IDataResponse<JobDTO>();
        try {
            dataResponse.setData(jobService.findJobsByCompany(companyId, flag));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Stages Successfully Retrieved","Retrieved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
