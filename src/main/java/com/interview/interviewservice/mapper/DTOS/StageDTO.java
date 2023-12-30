package com.interview.interviewservice.mapper.DTOS;

import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.mapper.DTOS.core.BaseDTO;
import lombok.Data;

import java.util.Objects;

@Data
public class StageDTO extends BaseDTO {
    private String color;
    private String description;
    private String companyId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StageDTO stage = (StageDTO) o;
        return Objects.equals(this.getId(), stage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
