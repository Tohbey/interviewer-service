package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.model.RoleEnum;
import com.interview.interviewservice.resource.BaseResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(LookupController.BASE_URL)
public class LookupController {

    public static final String BASE_URL = BaseResource.API+BaseResource.RELATIVEPATH+"lookup"+BaseResource.RELATIVEPATH;

    @GetMapping(value = "roles")
    public IDataResponse getRoles(){
        IDataResponse dataResponse = new IDataResponse();
        RoleEnum[] roles = RoleEnum.values();
        dataResponse.setValid(true);
        dataResponse.setData(Collections.singletonList(roles));
        dataResponse.addMessage(new GlobalMessage("Role Successfully Retrieved","Retrieved", Message.Severity.SUCCESS));
        return dataResponse;
    }
}
