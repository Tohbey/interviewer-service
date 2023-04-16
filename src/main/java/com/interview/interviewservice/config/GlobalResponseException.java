package com.interview.interviewservice.config;


import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.Util.IDataResponse;
import com.interview.interviewservice.service.impl.CompanyServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RestController
public class GlobalResponseException  extends ResponseEntityExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(GlobalResponseException.class);


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        IDataResponse response = new IDataResponse<>();

        response.addMessage(ex.getMessage());
        response.addMessage(ex.getLocalizedMessage());
        response.setValid(false);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
