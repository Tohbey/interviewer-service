package com.interview.interviewservice.model;

public interface Message {
    String defaultMsg = "Error occurred while processing transaction.";

    String defaultSuccessMsg = "Successfully processed transaction.";

    String successMsg = "Successfully processed %s transaction.";

    String getSummary();

    void setSummary(String summary);

    String getDetail();

    void setDetail(String detail);

    Message.Severity getSeverity();

    void setSeverity(Message.Severity severity);

    enum Severity {
        SUCCESS,
        INFO,
        WARNING,
        ERROR,
        FATAL,
        DANGER,
        ACCESS_DENIED;

        Severity() {
        }
    }
}
