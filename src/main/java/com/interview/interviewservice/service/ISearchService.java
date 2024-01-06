package com.interview.interviewservice.service;

import com.interview.interviewservice.Util.ResultQuery;

import java.io.IOException;

public interface ISearchService {
    ResultQuery searchFromQuery(String query, String[] fields) throws IOException;
}
