package com.interview.interviewservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.Util.KeyValuePair;
import com.interview.interviewservice.Util.ResultQuery;
import com.interview.interviewservice.dtos.ElasticSearchResponse;
import com.interview.interviewservice.elastic.StageModel;
import com.interview.interviewservice.elastic.UserModel;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.mapper.DTOS.StageDTO;
import com.interview.interviewservice.mapper.mappers.StageMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.StageRepository;
import com.interview.interviewservice.service.AuthenticationService;
import com.interview.interviewservice.service.ISearchService;
import com.interview.interviewservice.service.StageService;
import com.interview.interviewservice.service.UserContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StageServiceImpl implements StageService {

    private final StageRepository stageRepository;

    private final StageMapper stageMapper;

    private final CompanyRepository companyRepository;

    private final UserContextService userContextService;

    @Autowired
    private ISearchService iSearchService;


    public StageServiceImpl(StageRepository stageRepository,
                            StageMapper stageMapper,
                            CompanyRepository companyRepository,
                            UserContextService userContextService) {
        this.stageRepository = stageRepository;
        this.stageMapper = stageMapper;
        this.companyRepository = companyRepository;
        this.userContextService = userContextService;
    }


    @Override
    public void create(StageDTO stageDTO) throws CustomException {
        validate(stageDTO);
        Stage stage = mapper(stageDTO);
        stage.setFlag(Flag.ENABLED);

        Company company = this.companyRepository.findCompanyByCompanyId(stageDTO.getCompanyId());
        stage.setCompany(company);

        stageRepository.save(stage);
    }

    @Override
    public StageDTO find(Long stageId) throws CustomException {
        Optional<Stage> stage = stageRepository.findById(stageId);
        if(stage.isPresent()){
            StageDTO stageDTO = stageMapper.stageToStageDTO(stage.get());
            stageDTO.setCompanyId(stage.get().getCompany().getCompanyId());
            return stageDTO;
        }else{
            throw new CustomException("Stage Not found");
        }
    }

    @Override
    public void update(StageDTO stageDTO) throws CustomException {
        Optional<Stage> savedStage= stageRepository.findById(stageDTO.getId());
        if(savedStage.isPresent()){
            validateUpdate(stageDTO, savedStage.get());
            Stage stage = mapper(stageDTO);
            stage.setFlag(savedStage.get().getFlag());
            Company company = this.companyRepository.findCompanyByCompanyId(stageDTO.getCompanyId());
            stage.setCompany(company);

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
            stage.get().setLastModifiedDate(new Date());
            stage.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());

            stageRepository.save(stage.get());
        }else{
            throw new CustomException("Stage Not found");
        }
    }

    @Override
    public List<StageDTO> findStagesByCompany(String companyId, Flag flag) throws CustomException {
        List<StageDTO> stageDTOS = new ArrayList<>();
        Company company = this.companyRepository.findCompanyByCompanyId(companyId);

        if(Objects.isNull(company)){
            throw new CustomException("Company detail not found");
        }

        List<Stage> stages = stageRepository.findAllByCompanyAndFlag(company, flag);

        stages.forEach(stage -> {
            StageDTO stageDTO = stageMapper.stageToStageDTO(stage);
            stageDTO.setCompanyId(companyId);

            stageDTOS.add(stageDTO);
        });

        return stageDTOS;
    }

    @Override
    public List<KeyValuePair> stageSearch(String query, String companyId) throws Exception {
        List<KeyValuePair> results = new ArrayList<>();
        String[] STAGE_FIELDS = {"description"};
        ResultQuery resultQuery = iSearchService.searchFromQuery(query, STAGE_FIELDS, "stage/", companyId);
        if(Objects.nonNull(resultQuery.getElements())){
            try{
                ObjectMapper objectMapper = new ObjectMapper();
                List<ElasticSearchResponse<StageModel>> elasticSearchResponses = objectMapper.readValue(resultQuery.getElements(),
                        new TypeReference<List<ElasticSearchResponse<StageModel>>>() {});

                if(!elasticSearchResponses.isEmpty()){
                    for(ElasticSearchResponse elasticSearchResponse: elasticSearchResponses){
                        StageModel stageModel = (StageModel) elasticSearchResponse.getSource();
                        KeyValuePair keyValuePair = new KeyValuePair(stageModel.getId(), stageModel.getDescription());
                        results.add(keyValuePair);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }

        }
        return results;
    }

    private void validate(StageDTO stageDTO) throws CustomException {
        if(Objects.isNull(stageDTO.getCompanyId())){
            throw new CustomException("Company detail is missing");
        }

        Company company = this.companyRepository.findCompanyByCompanyId(stageDTO.getCompanyId());
        if(Objects.isNull(company)){
            throw new CustomException("Company detail not found");
        }

        if(stageRepository.existsByDescriptionAndCompany(stageDTO.getDescription(),company)){
            throw new CustomException("Stage Already exist");
        }

    }

    private void validateUpdate(StageDTO stageDTO, Stage savedStage) throws CustomException {
        Company company = this.companyRepository.findCompanyByCompanyId(stageDTO.getCompanyId());

        if(Objects.isNull(company)){
            throw new CustomException("Company detail not found");
        }

        if(!savedStage.getDescription().equalsIgnoreCase(stageDTO.getDescription())){
            if(stageRepository.existsByDescriptionAndCompany(stageDTO.getDescription(),company)){
                throw new CustomException("Stage Already exist");
            }
        }
    }

    private Stage mapper(StageDTO stageDTO) throws CustomException {
        Stage stage = stageMapper.stageDTOToStage(stageDTO);
        if(Objects.isNull(stage.getId())){
            stage.setCreatedDate(new Date());
            stage.setCreatedBy(userContextService.getCurrentUserDTO().getFullname());
        }else{
            stage.setLastModifiedDate(new Date());
            stage.setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
        }

        return stage;
    }
}
