package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.mapper.DTOS.StageDTO;
import com.interview.interviewservice.mapper.mappers.StageMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.StageRepository;
import com.interview.interviewservice.service.StageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StageServiceImpl implements StageService {

    private final StageRepository stageRepository;

    private final StageMapper stageMapper;

    private final CompanyRepository companyRepository;

    public StageServiceImpl(StageRepository stageRepository, StageMapper stageMapper, CompanyRepository companyRepository) {
        this.stageRepository = stageRepository;
        this.stageMapper = stageMapper;
        this.companyRepository = companyRepository;
    }


    @Override
    public void create(StageDTO stageDTO) throws CustomException {
        validate(stageDTO);
        Stage stage = stageMapper.stageDTOToStage(stageDTO);
        stage.setFlag(Flag.ENABLED);

        Optional<Company> company = companyRepository.findById(stageDTO.getCompanyId());
        stage.setCompany(company.get());

        stageRepository.save(stage);
    }

    @Override
    public StageDTO find(Long stageId) throws CustomException {
        Optional<Stage> stage = stageRepository.findById(stageId);
        if(stage.isPresent()){
            return stageMapper.stageToStageDTO(stage.get());
        }else{
            throw new CustomException("Stage Not found");
        }
    }

    @Override
    public void update(StageDTO stageDTO) throws CustomException {
        Optional<Stage> savedStage= stageRepository.findById(stageDTO.getId());
        if(savedStage.isPresent()){
            validateUpdate(stageDTO, savedStage.get());
            Stage stage = stageMapper.stageDTOToStage(stageDTO);
            Optional<Company> company = companyRepository.findById(stageDTO.getCompanyId());
            stage.setCompany(company.get());

            stageRepository.save(stage);
        }else{
            throw new CustomException("Stage Not found");
        }
    }

    @Override
    public void delete(Long stageId) throws CustomException {
        Optional<Stage> stage = stageRepository.findById(stageId);
        if(stage.isPresent()){
            stage.get().setFlag(Flag.DISABLED);
            stageRepository.save(stage.get());
        }else{
            throw new CustomException("Stage Not found");
        }
    }

    @Override
    public List<StageDTO> findStagesByCompany(Long companyId, Flag flag) throws CustomException {
        List<StageDTO> stageDTOS = new ArrayList<>();
        Optional<Company> company = companyRepository.findById(companyId);
        if(company.isEmpty()){
            throw new CustomException("Company detail not found");
        }

        List<Stage> stages = stageRepository.findAllByCompanyAndFlag(company.get(), flag);

        stages.forEach(stage -> {
            StageDTO stageDTO = stageMapper.stageToStageDTO(stage);
            stageDTO.setCompanyId(companyId);

            stageDTOS.add(stageDTO);
        });

        return stageDTOS;
    }

    private void validate(StageDTO stageDTO) throws CustomException {
        if(Objects.isNull(stageDTO.getCompanyId())){
            throw new CustomException("Company detail is missing");
        }

        Optional<Company> company = companyRepository.findById(stageDTO.getCompanyId());
        if(company.isEmpty()){
            throw new CustomException("Company detail not found");
        }

        if(stageRepository.existsByDescriptionAndCompany(stageDTO.getDescription(),company.get())){
            throw new CustomException("Stage Already exist");
        }

    }

    private void validateUpdate(StageDTO stageDTO, Stage savedStage) throws CustomException {
        Optional<Company> company = companyRepository.findById(stageDTO.getCompanyId());
        if(company.isEmpty()){
            throw new CustomException("Company detail not found");
        }

        if(!savedStage.getDescription().equalsIgnoreCase(stageDTO.getDescription())){
            if(stageRepository.existsByDescriptionAndCompany(stageDTO.getDescription(),company.get())){
                throw new CustomException("Stage Already exist");
            }
        }
    }
}
