package com.interview.interviewservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {
    @Bean
    public CommonsRequestLoggingFilter loggingFilter(){
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(100000);
        loggingFilter.setIncludeHeaders(false);
        loggingFilter.setIncludePayload(false);
        loggingFilter.setAfterMessagePrefix("REQUEST DATA: ");
        return loggingFilter;
    }
}
