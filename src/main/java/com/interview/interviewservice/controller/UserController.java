package com.interview.interviewservice.controller;

import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.model.Message;
import com.interview.interviewservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

//  Testing Completed.
@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL="/api/v1/user";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public IDataResponse<User> createUser(@RequestBody @Valid UserDTO userDTO){
        IDataResponse<User> dataResponse = new IDataResponse<User>();
        try {
            userService.create(userDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("User Successfully Created","Saved", Message.Severity.INFO));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/find/{user}")
    public IDataResponse<UserDTO> findUser(@PathVariable("user") Long userId){
        IDataResponse<UserDTO> dataResponse = new IDataResponse<UserDTO>();
        try {
            dataResponse.setData(Collections.singletonList(userService.find(userId)));
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("User Successfully Retrieved","Retrieved", Message.Severity.SUCCESS));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{user}")
    public IDataResponse deleteUser(@PathVariable("user") Long userId){
        IDataResponse dataResponse = new IDataResponse();
        try {
            userService.delete(userId);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("User Successfully Deleted","Deleted", Message.Severity.SUCCESS));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public IDataResponse updateUser(@RequestBody UserDTO userDTO){
        IDataResponse dataResponse = new IDataResponse();
        try {
            userService.update(userDTO);
            dataResponse.setValid(true);
            dataResponse.addMessage(new GlobalMessage("User Successfully Updated","Deleted", Message.Severity.SUCCESS));
        }catch (Exception e) {
            e.printStackTrace();
            dataResponse.setValid(false);
            dataResponse.addMessage(new GlobalMessage(e.getMessage(), null, Message.Severity.ERROR));
        }
        return dataResponse;
    }
}
