package com.interview.interviewservice.controller;


import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.dtos.*;
import com.interview.interviewservice.entity.RefreshToken;
import com.interview.interviewservice.jwt.JwtUtils;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.resource.BaseResource;
import com.interview.interviewservice.service.AuthenticationService;
import com.interview.interviewservice.service.RefreshTokenService;
import com.interview.interviewservice.service.UserContextService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


//  Testing Completed.
@RestController
@RequestMapping(AuthenticationController.BASE_URL)
public class AuthenticationController {
    public static final String BASE_URL= BaseResource.API+BaseResource.RELATIVEPATH+"auth"+BaseResource.RELATIVEPATH;

    private final AuthenticationService authenticationService;

    private final UserContextService userContextService;

    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(AuthenticationService authenticationService, UserContextService userContextService, RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.userContextService = userContextService;
        this.refreshTokenService = refreshTokenService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "login")
    public IDataResponse createAuthenticationToken(@RequestBody @Valid AuthenticationRequest authenticationRequest) throws Exception {
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

    @RequestMapping(method = RequestMethod.POST, value = "refresh-token")
    public IDataResponse RefreshToken(@RequestBody @Valid TokenRefreshRequest tokenRefreshRequest) throws Exception {
        IDataResponse dataResponse = new IDataResponse();
        try {
            AuthenticationResponse response = refreshTokenService.refresh(tokenRefreshRequest.getRefreshToken());
            dataResponse.setData(Collections.singletonList(response));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("Token Refreshed Successfully","Refresh Token", Message.Severity.INFO));
        } catch (Exception e) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
            e.printStackTrace();
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "verify")
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

    @RequestMapping(method = RequestMethod.PATCH, value = "change-password")
    public IDataResponse changePassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
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

    @RequestMapping(method = RequestMethod.POST, value = "forgot")
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

    @RequestMapping(method = RequestMethod.POST, value = "reset")
    public IDataResponse resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
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


    @RequestMapping(method = RequestMethod.GET, value = "getUserInfo")
    public IDataResponse CurrentUserInfo() {
        IDataResponse dataResponse = new IDataResponse();
        try {
            dataResponse.setData(Collections.singletonList(userContextService.getCurrentUserDTO()));
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
