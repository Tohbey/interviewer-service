package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.mapper.DTOS.StageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StageMapper {
    StageMapper INSTANCE = Mappers.getMapper(StageMapper.class);

    StageDTO stageToStageDTO(Stage stage);

    Stage stageDTOToStage(StageDTO stageDTO);
}
