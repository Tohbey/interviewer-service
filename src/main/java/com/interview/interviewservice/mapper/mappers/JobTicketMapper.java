package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.JobTicket;
import com.interview.interviewservice.mapper.DTOS.JobTicketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobTicketMapper {
    JobTicketMapper INSTANCE = Mappers.getMapper(JobTicketMapper.class);

    JobTicketDTO jobTicketToJobTicketDTO(JobTicket jobTicket);

    JobTicket jobTicketDTOToJobTicket(JobTicketDTO jobTicketDTO);
}
