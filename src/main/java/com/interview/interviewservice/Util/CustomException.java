package com.interview.interviewservice.Util;

import com.interview.interviewservice.model.Message;

public class CustomException extends Exception{

    protected Message[] messages;

    public CustomException() {

    }

    public CustomException(String message) {

        super(message);
    }

    public CustomException(String msg, Throwable cause) {

        super(msg, cause);
    }

    public Message[] getMessages() {

        return this.messages;
    }
}
