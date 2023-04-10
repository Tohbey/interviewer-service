package com.interview.interviewservice.mapper.mappers;

import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.mapper.DTOS.CandidateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CandidateMapper {
    CandidateMapper INSTANCE = Mappers.getMapper(CandidateMapper.class);

    CandidateDTO candidateToCandidateDTO(Candidate candidate);

    Candidate candidateDTOToCandidate(CandidateDTO candidateDTO);

}
