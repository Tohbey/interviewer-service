package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Candidate;
import com.interview.interviewservice.entity.EducationalHistory;
import com.interview.interviewservice.entity.EmploymentHistory;
import com.interview.interviewservice.mapper.DTOS.*;
import com.interview.interviewservice.mapper.mappers.CandidateMapper;
import com.interview.interviewservice.mapper.mappers.EducationalHistoryMapper;
import com.interview.interviewservice.mapper.mappers.EmploymentHistoryMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.model.RoleEnum;
import com.interview.interviewservice.repository.CandidateRepository;
import com.interview.interviewservice.repository.EducationalHistoryRepository;
import com.interview.interviewservice.repository.EmploymentHistoryRepository;
import com.interview.interviewservice.service.CandidateService;
import com.interview.interviewservice.service.UserContextService;
import com.interview.interviewservice.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    private final CandidateMapper candidateMapper;

    private final EducationalHistoryMapper educationalHistoryMapper;

    private final EmploymentHistoryMapper employmentHistoryMapper;


    private final EmploymentHistoryRepository employmentHistoryRepository;

    private final EducationalHistoryRepository educationalHistoryRepository;

    private final UserService userService;

    private final UserContextService userContextService;

    public CandidateServiceImpl(CandidateRepository candidateRepository, CandidateMapper candidateMapper
            , EducationalHistoryMapper educationalHistoryMapper, EmploymentHistoryMapper employmentHistoryMapper,
                                EmploymentHistoryRepository employmentHistoryRepository,
                                EducationalHistoryRepository educationalHistoryRepository, UserService userService, UserContextService userContextService) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
        this.educationalHistoryMapper = educationalHistoryMapper;
        this.employmentHistoryMapper = employmentHistoryMapper;
        this.employmentHistoryRepository = employmentHistoryRepository;
        this.educationalHistoryRepository = educationalHistoryRepository;
        this.userService = userService;
        this.userContextService = userContextService;
    }

    @Override
    @Transactional
    public void create(CandidateDTO candidateDTO) throws CustomException, MessagingException {
        validation(candidateDTO);
        List<EducationalHistory> educationalHistories = new ArrayList<>();
        List<EmploymentHistory> employmentHistories = new ArrayList<>();
        Candidate candidate = candidateMapper.candidateDTOToCandidate(candidateDTO);

        candidate.setFlag(Flag.ENABLED);

        candidate = candidateRepository.save(candidate);
        Candidate finalCandidate = candidate;
        candidateDTO.getEmploymentHistories().forEach(employmentHistoryDto -> {
            EmploymentHistory employmentHistory = employmentHistoryMapper.employmentHistoryDtoToEmploymentHistory(employmentHistoryDto);
            employmentHistory.setCandidate(finalCandidate);
            employmentHistories.add(employmentHistory);
        });

        employmentHistoryRepository.saveAll(employmentHistories);

        candidateDTO.getEducationalHistories().forEach(educationalHistoryDto -> {
            EducationalHistory educationalHistory = educationalHistoryMapper.educationHistoryDtoToEducationHistory(educationalHistoryDto);
            educationalHistory.setCandidate(finalCandidate);
            educationalHistories.add(educationalHistory);
        });

        educationalHistoryRepository.saveAll(educationalHistories);

        RoleDTO roleDTO = new RoleDTO(3L, RoleEnum.CANDIDATE);
        UserDTO userDTO = new UserDTO(
                candidate.getSurname(),
                candidate.getOtherNames(),
                candidate.getSurname().concat(candidate.getOtherNames()),
                candidate.getAddress(),
                candidate.getPhoneNumber(),
                candidate.getEmail(),
                "",
                null,
                true,
                "", roleDTO, null, false);

        userService.create(userDTO);

    }

    @Override
    public void delete(Long candidateId) throws CustomException {
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if(candidate.isPresent()){
            candidate.get().setFlag(Flag.DISABLED);
            candidate.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
            candidate.get().setLastModifiedDate(new Date());
            candidateRepository.save(candidate.get());

            if(Objects.nonNull(candidate.get().getUser())){
                userService.delete(candidate.get().getUser().getId());
            }
        }else{
            throw new CustomException("Candidate Not found");
        }
    }

    @Override
    public CandidateDTO find(Long candidateId) throws Exception {

        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if(candidate.isPresent()){
            return candidateDtoMapper(candidate.get());
        }else{
            throw new CustomException("Candidate Not found");
        }
    }

    @Override
    public CandidateDTO candidateDtoMapper(Candidate candidate) {
        List<EducationalHistory> educationalHistories = educationalHistoryRepository.findAllByCandidate(candidate);
        List<EmploymentHistory> employmentHistories = employmentHistoryRepository.findAllByCandidate(candidate);

        CandidateDTO candidateDTO = candidateMapper.candidateToCandidateDTO(candidate);
        if(!educationalHistories.isEmpty()){
            educationalHistories.forEach(educationalHistory -> {
                EducationalHistoryDTO educationalHistoryDTO = educationalHistoryMapper.educationHistoryToEducationHistoryDto(educationalHistory);
                educationalHistoryDTO.setCandidateId(candidate.getId());
                candidateDTO.getEducationalHistories().add(educationalHistoryDTO);
            });
        }

        if(!employmentHistories.isEmpty()){
            employmentHistories.forEach(employmentHistory -> {
                EmploymentHistoryDTO employmentHistoryDTO = employmentHistoryMapper.employmentHistoryToEmploymentHistoryDto(employmentHistory);
                employmentHistoryDTO.setCandidateId(candidate.getId());
                candidateDTO.getEmploymentHistories().add(employmentHistoryDTO);
            });
        }

        return candidateDTO;

    }


    @Override
    public void update(CandidateDTO candidateDTO) throws CustomException {
        List<EducationalHistory> educationalHistories = new ArrayList<>();
        List<EmploymentHistory> employmentHistories = new ArrayList<>();
        Optional<Candidate> savedCandidate = candidateRepository.findById(candidateDTO.getId());

        if(savedCandidate.isEmpty()) {
            throw new CustomException("Candidate Doesnt Exist");
        }
        validateUpdate(candidateDTO, savedCandidate.get());
        Candidate candidate = candidateMapper.candidateDTOToCandidate(candidateDTO);

        candidate.setFlag(Flag.ENABLED);

        candidate = candidateRepository.save(candidate);
        Candidate finalCandidate = candidate;

        employmentHistoryRepository.deleteAllByCandidate(finalCandidate);
        educationalHistoryRepository.deleteAllByCandidate(candidate);

        candidateDTO.getEmploymentHistories().forEach(employmentHistoryDto -> {
            EmploymentHistory employmentHistory = employmentHistoryMapper.employmentHistoryDtoToEmploymentHistory(employmentHistoryDto);
            employmentHistory.setCandidate(finalCandidate);
            employmentHistories.add(employmentHistory);
        });

        employmentHistoryRepository.saveAll(employmentHistories);

        candidateDTO.getEducationalHistories().forEach(educationalHistoryDto -> {
            EducationalHistory educationalHistory = educationalHistoryMapper.educationHistoryDtoToEducationHistory(educationalHistoryDto);
            educationalHistory.setCandidate(finalCandidate);
            educationalHistories.add(educationalHistory);
        });

        educationalHistoryRepository.saveAll(educationalHistories);
    }

    private void validation(CandidateDTO candidateDTO) throws CustomException {
        if(candidateRepository.existsByEmailIgnoreCase(candidateDTO.getEmail())){
            throw new CustomException("Email Already Exist");
        }

        if (candidateRepository.existsByPhoneNumberIgnoreCase(candidateDTO.getPhoneNumber())){
            throw new CustomException("Phone Number Already Exist");
        }

        if(candidateDTO.getEducationalHistories().size() < 1){
            throw new CustomException("Education History cant be empty");
        }

        if(candidateDTO.getEmploymentHistories().size() < 1){
            throw new CustomException("Employment History cant be empty");
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
