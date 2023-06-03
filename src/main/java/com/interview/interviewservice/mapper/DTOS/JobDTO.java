package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import com.interview.interviewservice.model.JobType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class JobDTO extends BaseDTO {

    private String title;

    private String jobId;

    private String section;

    private String location;

    private String country;

    private JobType jobType;

    private Set<StageDTO> stages = new HashSet<>();

    private String description;

    private Set<String> qualifications = new HashSet<>();

    private Set<String> responsibilities = new HashSet<>();

    private Set<String> requirements = new HashSet<>();

    private Set<String> skills = new HashSet<>();

    private Date deadLine;

    private String companyId;
}
