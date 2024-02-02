package com.interview.interviewservice.elastic;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "stage")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StageModel {
    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Text, name = "companyId")
    private String companyId;

    @JsonIgnore
    private String _class;
}
