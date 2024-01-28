package com.interview.interviewservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElasticSearchResponse<M> {
    @JsonProperty("_index")
    private String index;

    @JsonProperty("_source")
    private M source;

    @JsonProperty("_id")
    private String id;

    @JsonProperty("_score")
    private double score;
}
