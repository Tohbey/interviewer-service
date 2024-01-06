package com.interview.interviewservice.repository.eRepository;

import com.interview.interviewservice.elastic.UserModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserERepo extends ElasticsearchRepository<UserModel, Long> {
}
