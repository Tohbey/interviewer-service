package com.interview.interviewservice.mapper.mappers;


import com.interview.interviewservice.entity.CustomMessage;
import com.interview.interviewservice.mapper.DTOS.CustomMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomMessageMapper {
    CustomMessageMapper INSTANCE = Mappers.getMapper(CustomMessageMapper.class);

    CustomMessageDTO customMessageToCustomMessageDTO(CustomMessage customMessage);

    CustomMessage customMessageDTOToCustomMessage(CustomMessageDTO customMessageDTO);
}
