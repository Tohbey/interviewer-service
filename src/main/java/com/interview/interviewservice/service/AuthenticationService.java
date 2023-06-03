package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.dtos.*;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.UserDTO;

import java.util.Optional;

public interface AuthenticationService {
    AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) throws CustomException;

    void verifyUser(String email, String token) throws CustomException;

    Boolean checkIfValidOldPassword(User user, String oldPassword);

    void changePassword(ForgotPasswordRequest forgotPasswordRequest) throws CustomException;

    void recover(String email) throws CustomException;

    Optional<User> reset(String email, String token) throws CustomException;

    void resetPassword(ResetPasswordRequest resetPasswordRequest) throws CustomException;
}
