package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.mapper.DTOS.CandidateDTO;
import com.interview.interviewservice.mapper.DTOS.RoleDTO;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.mapper.mappers.CandidateMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.model.RoleEnum;
import com.interview.interviewservice.repository.CandidateRepository;
import com.interview.interviewservice.service.CandidateService;
import com.interview.interviewservice.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    private final CandidateMapper candidateMapper;

    private final UserService userService;
    public CandidateServiceImpl(CandidateRepository candidateRepository, CandidateMapper candidateMapper, UserService userService) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void create(CandidateDTO candidateDTO) throws CustomException, MessagingException {
        validation(candidateDTO);

        Candidate candidate = candidateMapper.candidateDTOToCandidate(candidateDTO);
        candidate.setFlag(Flag.ENABLED);

        candidate = candidateRepository.save(candidate);

        RoleDTO roleDTO = new RoleDTO(3L, RoleEnum.CANDIDATE);

        UserDTO userDTO = new UserDTO(
                candidate.getSurname(),
                candidate.getOtherNames(),
                candidate.getSurname().concat(candidate.getOtherNames()),
                candidate.getAddress(),
                candidate.getPhoneNumber(),
                candidate.getEmail(),
                "",
                "",
                true,
                "", roleDTO, null, false);

        userService.create(userDTO);

    }

    @Override
    public void delete(Long candidateId) throws Exception {
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if(candidate.isPresent()){
            candidate.get().setFlag(Flag.DISABLED);
            candidate.get().setLastModifiedDate(new Date());
            candidateRepository.save(candidate.get());

            if(Objects.nonNull(candidate.get().getUser())){
                userService.delete(candidate.get().getUser().getId());
            }
        }else{
            throw new Exception("Candidate Not found");
        }
    }

    @Override
    public CandidateDTO find(Long candidateId) throws Exception {
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if(candidate.isPresent()){
            return candidateMapper.candidateToCandidateDTO(candidate.get());
        }else{
            throw new Exception("Candidate Not found");
        }
    }

    @Override
    public void update(CandidateDTO candidateDTO) throws CustomException {
        Optional<Candidate> savedCandidate = candidateRepository.findById(candidateDTO.getId());

        if(savedCandidate.isEmpty()) {
            throw new CustomException("Candidate Doesnt Exist");
        }
        validateUpdate(candidateDTO, savedCandidate.get());
        Candidate candidate = candidateMapper.candidateDTOToCandidate(candidateDTO);

        candidateRepository.save(candidate);

    }

    private void validation(CandidateDTO candidateDTO) throws CustomException {
        if(candidateRepository.existsByEmailIgnoreCase(candidateDTO.getEmail())){
            throw new CustomException("Email Already Exist");
        }

        if (candidateRepository.existsByPhoneNumberIgnoreCase(candidateDTO.getPhoneNumber())){
            throw new CustomException("Phone Number Already Exist");
        }
    }

    private void validateUpdate(CandidateDTO candidateDTO, Candidate savedCandidate) throws CustomException {
        if(!savedCandidate.getEmail().equalsIgnoreCase(candidateDTO.getEmail())){
            if(candidateRepository.existsByEmailIgnoreCase(candidateDTO.getEmail())){
                throw new CustomException("Email Already Exist");
            }
        }

        if(!savedCandidate.getPhoneNumber().equalsIgnoreCase(candidateDTO.getPhoneNumber())){
            if (candidateRepository.existsByPhoneNumberIgnoreCase(candidateDTO.getPhoneNumber())){
                throw new CustomException("Phone Number Already Exist");
            }
        }
    }
}
