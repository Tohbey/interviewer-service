package com.interview.interviewservice.exceptions;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.Util.GlobalMessage;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.model.Message;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalResponseException  extends ResponseEntityExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(GlobalResponseException.class);


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        IDataResponse response = new IDataResponse<>();

        response.addMessage(new GlobalMessage(ex.getMessage(), null, Message.Severity.ERROR));
        response.setValid(false);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        IDataResponse response = new IDataResponse<>();

        response.addMessage(new GlobalMessage(ex.getMessage(), null, Message.Severity.ERROR));
        response.setValid(false);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        IDataResponse response = new IDataResponse<>();

        response.addMessage(new GlobalMessage(ex.getMessage(), null, Message.Severity.ERROR));
        response.setValid(false);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        IDataResponse response = new IDataResponse<>();

        response.addMessage(new GlobalMessage(ex.getMessage(), null, Message.Severity.ERROR));
        response.setValid(false);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex){
        IDataResponse response = new IDataResponse<>();

        response.setValid(false);
        response.addMessage(new GlobalMessage(ex.getMessage(), null, Message.Severity.ERROR));

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Object> handleMessagingException(MessagingException ex){
        IDataResponse response = new IDataResponse<>();

        response.setValid(false);
        response.addMessage(new GlobalMessage(ex.getMessage(), null, Message.Severity.ERROR));

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
