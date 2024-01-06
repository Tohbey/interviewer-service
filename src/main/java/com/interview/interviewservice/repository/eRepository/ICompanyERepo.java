package com.interview.interviewservice.repository.eRepository;

import com.interview.interviewservice.elastic.CompanyModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyERepo extends ElasticsearchRepository<CompanyModel, Long> {
}
