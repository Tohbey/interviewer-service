package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.config.CustomDetail;
import com.interview.interviewservice.config.CustomDetailService;
import com.interview.interviewservice.dtos.AuthenticationRequest;
import com.interview.interviewservice.dtos.AuthenticationResponse;
import com.interview.interviewservice.dtos.ForgotPasswordRequest;
import com.interview.interviewservice.dtos.ResetPasswordRequest;
import com.interview.interviewservice.entity.Token;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.jwt.JwtUtils;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.mapper.mappers.UserMapper;
import com.interview.interviewservice.repository.TokenRepository;
import com.interview.interviewservice.repository.UserRepository;
import com.interview.interviewservice.service.AuthenticationService;
import com.interview.interviewservice.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final TokenRepository tokenRepository;


    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl .class);

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     UserRepository userRepository,
                                     TokenRepository tokenRepository,
                                     UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
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
    @Transactional
    public void verifyUser(String email, String token) throws Exception {
        //find user
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        //find token
        Optional<Token> rememberToken = tokenRepository.findTokenByUser(user.get());
        if (rememberToken.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        //validate the token
        if (!rememberToken.get().getToken().equals(token)) {
            throw new Exception("Incorrect Token");
        }

        //update user
        user.get().setIsActive(true);

        tokenRepository.deleteById(rememberToken.get().getId());

        userService.update(userService.mapper(user.get()));
    }

    @Override
    public Boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void changePassword(ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        Optional<User> user = getCurrent();

        if (!checkIfValidOldPassword(user.get(), forgotPasswordRequest.getOldPassword())) {
            throw new Exception("Invalid Old Password");
        }

        String pwd = forgotPasswordRequest.getNewPassword();
        String encryptPwd = passwordEncoder.encode(pwd);
        user.get().setPassword(encryptPwd);
        user.get().setIsNewUser(false);

        userRepository.save(user.get());
    }

    @Override
    public void recover(String email) throws CustomException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        String token = RandomStringUtils.randomAlphabetic(20);
        Token passwordRetrieve = new Token();
        passwordRetrieve.setToken(token);

        //adding 20 minutes to the current time
        Calendar present = Calendar.getInstance();
        long timeInSecs = present.getTimeInMillis();
        Date expiredAt = new Date(timeInSecs + (20 * 60 * 1000));

        //save token
        passwordRetrieve.setUser(user.get());
        passwordRetrieve.setExpiredAt(expiredAt);
        tokenRepository.save(passwordRetrieve);
    }

    @Override
    public Optional<User> reset(String email, String token) throws Exception {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        Optional<Token> passwordRetrieve = tokenRepository.findTokenByUser(user.get());
        if (passwordRetrieve.isEmpty()) {
            throw new CustomException("Token Not Found. for TOKEN value " + token);
        }

        if (!token.equals(passwordRetrieve.get().getToken())) {
            throw new Exception("Incorrect Token");
        }

        return user;
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws Exception {
        Optional<User> user = reset(resetPasswordRequest.getEmail(), resetPasswordRequest.getToken());

        String encryptPwd = passwordEncoder.encode(resetPasswordRequest.getPassword());
        user.get().setPassword(encryptPwd);

        Optional<Token> passwordRetrieve = tokenRepository.findTokenByTokenAndUser(resetPasswordRequest.getToken(), user.get());
        if (passwordRetrieve.isEmpty()) {
            throw new CustomException("Token Not Found. for TOKEN value " + resetPasswordRequest.getToken());
        }
        tokenRepository.deleteById(passwordRetrieve.get().getId());

        user.map(user1 -> {
            user1.setPassword(user.get().getPassword());
            return userRepository.save(user1);
        }).orElseGet(() -> {
            return userRepository.save(user.get());
        });
    }

    @Override
    public UserDTO getCurrentUser() throws CustomException {
        CustomDetail userDetail = (CustomDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetail.getUsername();
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        return userService.mapper(user.get());
    }

    private Optional<User> getCurrent() throws CustomException {
        CustomDetail userDetail = (CustomDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetail.getUsername();
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User Not Found. for EMAIL value " + email);
        }

        return user;
    }
}
