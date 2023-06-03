package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.CandidateDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.resource.BaseResource;
import com.interview.interviewservice.service.CandidateService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RestController
@RequestMapping(value = CandidateController.BASE_URL)
public class CandidateController {

    public static final String BASE_URL= BaseResource.API+BaseResource.RELATIVEPATH+"candidate"+BaseResource.RELATIVEPATH;

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @RequestMapping(method = RequestMethod.POST, value = BaseResource.SAVE)
    public IDataResponse createCandidate(@RequestBody CandidateDTO candidateDTO) {
        IDataResponse dataResponse = new IDataResponse();
        try {
            candidateService.create(candidateDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Candidate Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = BaseResource.FIND+BaseResource.RELATIVEPATH+"{candidateId}")
    public IDataResponse<CandidateDTO> findCandidate(@PathVariable("candidateId") Long candidateId) {
        IDataResponse<CandidateDTO> dataResponse = new IDataResponse<CandidateDTO>();
        try {
            dataResponse.setData(Collections.singletonList(candidateService.find(candidateId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Candidate Successfully Retrieved","Retrieved", Message.Severity.SUCCESS));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = BaseResource.DELETE+BaseResource.RELATIVEPATH+"{candidateId}")
    public IDataResponse deleteCandidate(@PathVariable("candidateId") Long candidateId) {
        IDataResponse dataResponse = new IDataResponse();
        try {
            candidateService.delete(candidateId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Candidate Successfully Deleted","Deleted", Message.Severity.SUCCESS));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PUT, value = BaseResource.UPDATE)
    public IDataResponse updateCandidate(@RequestBody CandidateDTO candidateDTO) {
        IDataResponse dataResponse = new IDataResponse();
        try {
            candidateService.update(candidateDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Candidate Successfully Updated","Deleted", Message.Severity.SUCCESS));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
