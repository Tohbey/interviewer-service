package com.interview.interviewservice.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultQuery {
    private Float timeTook;
    private Integer numberOfResults;
    private String elements;
}
