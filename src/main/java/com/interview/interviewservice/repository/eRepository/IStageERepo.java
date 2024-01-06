package com.interview.interviewservice.repository.eRepository;

import com.interview.interviewservice.elastic.StageModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStageERepo extends ElasticsearchRepository<StageModel, Long> {
}
