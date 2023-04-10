package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.*;
import com.interview.interviewservice.mapper.DTOS.InterviewDTO;
import com.interview.interviewservice.mapper.mappers.InterviewMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.*;
import com.interview.interviewservice.service.InterviewService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;

    private final TeamRepository teamRepository;

    private final StageRepository stageRepository;

    private final CompanyRepository companyRepository;

    private final JobRepository jobRepository;

    private final CandidateRepository candidateRepository;

    private final InterviewMapper interviewMapper;

    public InterviewServiceImpl(InterviewRepository interviewRepository, TeamRepository teamRepository, StageRepository stageRepository,
                                CompanyRepository companyRepository,
                                JobRepository jobRepository,
                                CandidateRepository candidateRepository, InterviewMapper interviewMapper) {
        this.interviewRepository = interviewRepository;
        this.teamRepository = teamRepository;
        this.stageRepository = stageRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.candidateRepository = candidateRepository;
        this.interviewMapper = interviewMapper;
    }

    @Override
    public void create(InterviewDTO interviewDTO) throws CustomException {
        validate(interviewDTO);
        Interview interview = interviewMapper.interviewDTOToInterview(interviewDTO);
        interview.setFlag(Flag.ENABLED);
        Company company = companyRepository.findCompanyByCompanyId(interviewDTO.getCompanyId());
        interview.setCompany(company);

        interviewRepository.save(interview);
    }

    @Override
    public InterviewDTO findInterview(Long interviewId) throws CustomException {
        Optional<Interview> interview = interviewRepository.findById(interviewId);
        if(interview.isPresent()) {
            InterviewDTO interviewDTO = interviewMapper.interviewToInterviewDTO(interview.get());
            interviewDTO.setCompanyId(interview.get().getCompany().getCompanyId());

            return interviewDTO;
        }else{
            throw new CustomException("Interview details not found");
        }
    }

    @Override
    public void delete(Long interviewId) throws CustomException {
        Optional<Interview> interview = interviewRepository.findById(interviewId);
        if(interview.isPresent()) {
            interview.get().setFlag(Flag.DISABLED);
            interviewRepository.save(interview.get());
        }else{
            throw new CustomException("Interview details not found");
        }
    }

    @Override
    public void update(InterviewDTO interviewDTO) throws CustomException {
        Optional<Interview> savedInterview = interviewRepository.findById(interviewDTO.getId());
        if(savedInterview.isPresent()) {
            validateUpdate(interviewDTO,savedInterview.get());
            Interview interview = interviewMapper.interviewDTOToInterview(interviewDTO);

            interviewRepository.save(interview);
        }else{
            throw new CustomException("Interview details not found");
        }
    }

    @Override
    public List<InterviewDTO> findInterviewsByJob(Long jobId) throws CustomException {
        List<InterviewDTO> interviewDTOS = new ArrayList<InterviewDTO>();

        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isPresent()){
            List<Interview> interviews = interviewRepository.findAllByJob(job.get());

            interviews.forEach(interview -> {
                InterviewDTO interviewDTO = interviewMapper.interviewToInterviewDTO(interview);
                interviewDTO.setCompanyId(interview.getCompany().getCompanyId());
                interviewDTOS.add(interviewDTO);
            });

            return interviewDTOS;
        }else {
            throw new CustomException("Job details not found");
        }
    }

    @Override
    public List<InterviewDTO> findInterviewsByTeam(Long teamId) throws CustomException {
        List<InterviewDTO> interviewDTOS = new ArrayList<InterviewDTO>();
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            List<Interview> interviews = interviewRepository.findAllByTeam(team.get());

            interviews.forEach(interview -> {
                InterviewDTO interviewDTO = interviewMapper.interviewToInterviewDTO(interview);
                interviewDTO.setCompanyId(interview.getCompany().getCompanyId());
                interviewDTOS.add(interviewDTO);
            });

            return interviewDTOS;
        }else {
            throw new CustomException("Team details not found");
        }
    }

    @Override
    public InterviewDTO findInterviewByStageAndCandidate(Long stageId, Long candidateId) throws CustomException {
        InterviewDTO interviewDTO = new InterviewDTO();
        Optional<Candidate> candidate = candidateRepository.findById(interviewDTO.getCandidate().getId());
        Optional<Stage> stage = stageRepository.findById(interviewDTO.getStage().getId());

        if(candidate.isPresent() && stage.isPresent()) {
            Interview interview = interviewRepository.findAllByCandidateAndStage(candidate.get(), stage.get());
            if(Objects.nonNull(interview)){
                interviewDTO = interviewMapper.interviewToInterviewDTO(interview);
                interviewDTO.setCompanyId(interview.getCompany().getCompanyId());
            }
        }else if(stage.isEmpty()) {
            throw new CustomException("Stage details not found");
        }else{
            throw new CustomException("Candidate details not found");
        }

        return interviewDTO;
    }

    private void validate(InterviewDTO interviewDTO) throws CustomException {
        Company company =  companyRepository.findCompanyByCompanyId(interviewDTO.getCompanyId());
        if(Objects.isNull(company)){
            throw new CustomException("Company details not found");
        }

        Optional<Team> team = teamRepository.findById(interviewDTO.getTeam().getId());
        if(team.isEmpty()){
            throw new CustomException("Team details not found");
        }

        Optional<Stage> stage = stageRepository.findById(interviewDTO.getStage().getId());
        if(stage.isEmpty()){
            throw new CustomException("Stage details not found");
        }

        Optional<Job> job = jobRepository.findById(interviewDTO.getJob().getId());
        if(job.isEmpty()){
            throw new CustomException("Job details not found");
        }

        Optional<Candidate> candidate = candidateRepository.findById(interviewDTO.getCandidate().getId());
        if(candidate.isEmpty()){
            throw new CustomException("Candidate details not found");
        }


        if(interviewRepository.existsByTeamAndStageAndFlagAndCandidate(team.get(),stage.get(), Flag.ENABLED, candidate.get())){
            throw new CustomException("There is a current open interview for this candidate");
        }

    }

    private void validateUpdate(InterviewDTO interviewDTO, Interview savedInterview) throws CustomException {
        Company company =  companyRepository.findCompanyByCompanyId(interviewDTO.getCompanyId());
        if(!savedInterview.equals(company)){
            if(Objects.isNull(company)){
                throw new CustomException("Company details not found");
            }
        }

        Optional<Team> team = teamRepository.findById(interviewDTO.getTeam().getId());
        if(team.isEmpty()){
            throw new CustomException("Team details not found");
        }

        Optional<Stage> stage = stageRepository.findById(interviewDTO.getStage().getId());
        if(stage.isEmpty()){
            throw new CustomException("Stage details not found");
        }

        Optional<Job> job = jobRepository.findById(interviewDTO.getJob().getId());
        if(job.isEmpty()){
            throw new CustomException("Job details not found");
        }

        Optional<Candidate> candidate = candidateRepository.findById(interviewDTO.getCandidate().getId());
        if(candidate.isEmpty()){
            throw new CustomException("Candidate details not found");
        }

        if(!savedInterview.getTeam().equals(team.get()) || !savedInterview.getStage().equals(stage.get()) || !savedInterview.getCandidate().equals(candidate.get())){
            if(interviewRepository.existsByTeamAndStageAndFlagAndCandidate(team.get(),stage.get(), Flag.ENABLED, candidate.get())){
                throw new CustomException("There is a current open interview for this candidate");
            }
        }

    }
}
