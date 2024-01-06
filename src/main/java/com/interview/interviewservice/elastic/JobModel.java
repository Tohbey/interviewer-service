package com.interview.interviewservice.elastic;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "job")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobModel {
    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "title")
    private String title;

    @Field(type = FieldType.Text, name = "jobId")
    private String jobId;

    @Field(type = FieldType.Text, name = "section")
    private String section;

    @Field(type = FieldType.Text, name = "location")
    private String location;

    @Field(type = FieldType.Text, name = "country")
    private String country;

    @Field(type = FieldType.Text, name = "companyId")
    private String companyId;
}
