package com.interview.interviewservice.repository.eRepository;

import com.interview.interviewservice.elastic.TeamModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITeamERepo extends ElasticsearchRepository<TeamModel, Long> {
}
