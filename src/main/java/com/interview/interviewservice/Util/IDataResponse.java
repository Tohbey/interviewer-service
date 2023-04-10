package com.interview.interviewservice.Util;

import com.interview.interviewservice.model.DataResponse;
import com.interview.interviewservice.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IDataResponse<M> implements DataResponse<M> {

    private boolean valid;
    private List<M> data;
    private int totalRecords;
    private List<Message> messages;

    public IDataResponse(boolean valid) {
        this.valid = valid;
    }

    public final void addMessage(Message msg) {

        if (msg != null) {
            if (this.messages == null) {
                this.messages = new ArrayList();
            }
            this.messages.add(msg);
        }
    }


    public final void addMessage(String msg) {
        addMessage(msg, null);
    }

    public final void addMessage(String msg, Message.Severity severity) {
        addMessage(msg, null, severity);
    }


    public final void addMessage(String msg, String detail, Message.Severity severity) {

        if (msg != null || detail != null) {
            addMessage(new GlobalMessage(msg, detail, severity));
        }
    }
}
