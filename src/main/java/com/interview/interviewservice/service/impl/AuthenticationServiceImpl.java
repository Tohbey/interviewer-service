package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.config.CustomDetail;
import com.interview.interviewservice.config.CustomDetailService;
import com.interview.interviewservice.dtos.AuthenticationRequest;
import com.interview.interviewservice.dtos.AuthenticationResponse;
import com.interview.interviewservice.dtos.ForgotPasswordRequest;
import com.interview.interviewservice.dtos.ResetPasswordRequest;
import com.interview.interviewservice.entity.PasswordRetrieve;
import com.interview.interviewservice.entity.RememberToken;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.jwt.JwtUtils;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.mapper.mappers.UserMapper;
import com.interview.interviewservice.repository.PasswordRetrieveRepository;
import com.interview.interviewservice.repository.RememberTokenRepository;
import com.interview.interviewservice.repository.UserRepository;
import com.interview.interviewservice.service.AuthenticationService;
import com.interview.interviewservice.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenUtils;

    @Autowired
    private CustomDetailService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RememberTokenRepository rememberTokenRepository;

    private final UserMapper userMapper;

    private final UserService userService;

    private final PasswordRetrieveRepository passwordRetrieveRepository;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     UserRepository userRepository,
                                     RememberTokenRepository rememberTokenRepository,
                                     UserMapper userMapper,
                                     UserService userService, PasswordRetrieveRepository passwordRetrieveRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.rememberTokenRepository = rememberTokenRepository;
        this.userMapper = userMapper;
        this.userService = userService;
        this.passwordRetrieveRepository = passwordRetrieveRepository;
    }


    @Override
    public AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception {
        this.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final CustomDetail userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        final String token = jwtTokenUtils.generateJwtToken(userDetails);

        return new AuthenticationResponse(token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public void verifyUser(String email, String token) throws Exception {
        //find user
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        //find token
        Optional<RememberToken> rememberToken = rememberTokenRepository.findRememberTokenByToken(token);
        if (rememberToken.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        //validate the token
        if (!user.get().getToken().getToken().equals(rememberToken.get().getToken())) {
            throw new Exception("Incorrect Token");
        }

        //update user
        user.get().setIsActive(true);
        user.get().setToken(null);

        rememberTokenRepository.deleteById(rememberToken.get().getId());

//        userService.update(user.get(), user.get().getId());
    }

    @Override
    public Boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void changePassword(ForgotPasswordRequest forgotPasswordRequest) throws Exception {

    }

    @Override
    public void recover(String email) throws CustomException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        String token = RandomStringUtils.randomAlphabetic(20);
        PasswordRetrieve passwordRetrieve = new PasswordRetrieve();
        passwordRetrieve.setResetPasswordToken(token);

        //adding 20 minutes to the current time
        Calendar present = Calendar.getInstance();
        long timeInSecs = present.getTimeInMillis();
        Date expiredAt = new Date(timeInSecs + (20 * 60 * 1000));

        //save token
        passwordRetrieve.setUser(user.get());
        passwordRetrieve.setResetPasswordExpires(expiredAt);
        PasswordRetrieve savedPasswordRetrieveToken = passwordRetrieveRepository.save(passwordRetrieve);

        user.get().setPasswordRetrieve(savedPasswordRetrieveToken);


//        Optional<UserDTO> returnDTO = userService.update(user.get(), user.get().getId());
    }

    @Override
    public Optional<User> reset(String email, String token) throws Exception {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        Optional<PasswordRetrieve> passwordRetrieve = passwordRetrieveRepository.findPasswordRetrieveByResetPasswordToken(token);
        if (passwordRetrieve.isEmpty()) {
            throw new CustomException("Token Not Found. for TOKEN value " + token);
        }

        if (!user.get().getPasswordRetrieve().getResetPasswordToken().equals(passwordRetrieve.get().getResetPasswordToken())) {
            throw new Exception("Incorrect Token");
        }

        return user;
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws Exception {
        Optional<User> user = reset(resetPasswordRequest.getEmail(), resetPasswordRequest.getToken());

        String encryptPwd = passwordEncoder.encode(resetPasswordRequest.getPassword());
        user.get().setPassword(encryptPwd);

        Optional<PasswordRetrieve> passwordRetrieve = passwordRetrieveRepository.findPasswordRetrieveByResetPasswordToken(resetPasswordRequest.getToken());
        if (passwordRetrieve.isEmpty()) {
            throw new CustomException("Token Not Found. for TOKEN value " + resetPasswordRequest.getToken());
        }
        passwordRetrieveRepository.deleteById(passwordRetrieve.get().getId());

        user.get().setPasswordRetrieve(null);

//        userService.update();
    }

    @Override
    public Optional<User> getCurrentUser() throws CustomException {
        CustomDetail userDetail = (CustomDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetail.getUsername();
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        return Optional.of(user.get());
    }
}
