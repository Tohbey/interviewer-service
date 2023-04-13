package com.interview.interviewservice.controller;


import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.dtos.AuthenticationRequest;
import com.interview.interviewservice.dtos.AuthenticationResponse;
import com.interview.interviewservice.dtos.ForgotPasswordRequest;
import com.interview.interviewservice.dtos.ResetPasswordRequest;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping(AuthenticationController.BASE_URL)
public class AuthenticationController {
    public static final String BASE_URL="/api/v1/auth";

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @RequestMapping(method = RequestMethod.POST)
    public IDataResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        IDataResponse dataResponse = new IDataResponse();
        try {
            AuthenticationResponse response = this.authenticationService.createAuthenticationToken(authenticationRequest);
            dataResponse.setData(Collections.singletonList(response));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Login Successfully","Login", Message.Severity.INFO));
        } catch (Exception e) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
            e.printStackTrace();
        }
        return dataResponse;
    }


    @RequestMapping(method = RequestMethod.PATCH, value = "/verify")
    public IDataResponse verifyUser(@RequestParam(value = "email") String email, @RequestParam(value = "token") String token){
        IDataResponse dataResponse = new IDataResponse();
        try {
            authenticationService.verifyUser(email, token);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Verification Successfully","Verification", Message.Severity.INFO));
        } catch (Exception e) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
            e.printStackTrace();
        }

        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/change-password")
    public IDataResponse changePassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        IDataResponse dataResponse = new IDataResponse();
        try {
            authenticationService.changePassword(forgotPasswordRequest);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Password Updated  Successfully","Password Updated ", Message.Severity.INFO));
        } catch (Exception e) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
            e.printStackTrace();
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgot")
    public IDataResponse recover(@RequestParam(value = "email") String email) {
        IDataResponse dataResponse = new IDataResponse();
        try {
            authenticationService.recover(email);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("User Recover Process Started  Successfully","User Recover Process", Message.Severity.INFO));
        } catch (Exception e) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
            e.printStackTrace();
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/reset")
    public IDataResponse resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        IDataResponse dataResponse = new IDataResponse();
        try {
            authenticationService.resetPassword(resetPasswordRequest);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("User Password Updated Successfully","Password Update", Message.Severity.INFO));
        } catch (Exception e) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));

            e.printStackTrace();
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUserInfo")
    public IDataResponse CurrentUserInfo() {
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(Collections.singletonList(authenticationService.getCurrentUser()));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Current User Retrieved Successfully","Current User Info", Message.Severity.INFO));
        } catch (Exception e) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));

            e.printStackTrace();
        }
        return dataResponse;
    }
}
