package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.dtos.*;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.UserDTO;

import java.util.Optional;

public interface AuthenticationService {
    AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception;

    void verifyUser(String email, String token) throws Exception;

    Boolean checkIfValidOldPassword(User user, String oldPassword);

    void changePassword(ForgotPasswordRequest forgotPasswordRequest) throws Exception;

    void recover(String email) throws CustomException;

    Optional<User> reset(String email, String token) throws Exception;

    void resetPassword(ResetPasswordRequest resetPasswordRequest) throws Exception;

    Optional<User> getCurrentUser() throws CustomException;
}
