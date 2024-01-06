package com.interview.interviewservice.repository.eRepository;

import com.interview.interviewservice.elastic.JobModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IJobERepo extends ElasticsearchRepository<JobModel, Long> {
}
