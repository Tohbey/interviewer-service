package com.interview.interviewservice.Util;

import com.interview.interviewservice.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.interview.interviewservice.model.Message.Severity.ERROR;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalMessage implements Message{
    private String summary;

    private String detail;

    private Message.Severity severity;

    public static GlobalMessage forGlobalMessage(String error) {

        return new GlobalMessage(error, null, ERROR);
    }
    public static GlobalMessage forGlobalMessage(String error, Message.Severity severity) {

        return new GlobalMessage(error, null, severity);
    }
}
