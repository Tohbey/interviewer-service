package com.interview.interviewservice.elastic;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.annotation.Id;

@Document(indexName = "company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyModel {

    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "companyName")
    private String companyName;

    @Field(type = FieldType.Text, name = "country")
    private String country;

    @JsonIgnore
    private String _class;
}
