package com.interview.interviewservice.elastic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "team")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamModel {
    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "section")
    private String section;

    @Field(type = FieldType.Text, name = "companyId")
    private String companyId;

    @JsonIgnore
    private String _class;
}
