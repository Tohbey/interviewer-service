package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.mapper.DTOS.StageDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.service.StageService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;



//  Testing Completed.
@RestController
@RequestMapping(StageController.BASE_URL)
public class StageController {

    public static final String BASE_URL="/api/v1/stage";

    private final StageService stageService;


    public StageController(StageService stageService) {
        this.stageService = stageService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public IDataResponse createStage(@RequestBody StageDTO stageDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            stageService.create(stageDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Stage Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/find/{stageId}")
    public IDataResponse findStage(@PathVariable("stageId") Long stageId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(Collections.singletonList(stageService.find(stageId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Stage Successfully Retrieved","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public IDataResponse updateStage(@RequestBody StageDTO stageDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            stageService.update(stageDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Stage Successfully Retrieved","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{stageId}")
    public IDataResponse deleteStage(@PathVariable("stageId") Long stageId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            stageService.delete(stageId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Stage Successfully Retrieved","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
