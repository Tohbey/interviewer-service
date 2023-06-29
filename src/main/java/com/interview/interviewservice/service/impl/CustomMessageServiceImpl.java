package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.CustomMessage;
import com.interview.interviewservice.entity.JobApplication;
import com.interview.interviewservice.mapper.DTOS.CustomMessageDTO;
import com.interview.interviewservice.mapper.mappers.CustomMessageMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.CustomMessageRepository;
import com.interview.interviewservice.service.CustomMessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class CustomMessageServiceImpl implements CustomMessageService {

    private final CustomMessageRepository customMessageRepository;

    private final CompanyRepository companyRepository;

    private final CustomMessageMapper customMessageMapper;

    public CustomMessageServiceImpl(CustomMessageRepository customMessageRepository, CompanyRepository companyRepository, CustomMessageMapper customMessageMapper) {
        this.customMessageRepository = customMessageRepository;
        this.companyRepository = companyRepository;
        this.customMessageMapper = customMessageMapper;
    }

    @Override
    public void create(CustomMessageDTO customMessageDTO) throws CustomException {
        validation(customMessageDTO);
        CustomMessage customMessage = customMessageMapper.customMessageDTOToCustomMessage(customMessageDTO);

        Company company = companyRepository.findCompanyByCompanyId(customMessageDTO.getCompanyId());
        customMessage.setCompany(company);
        customMessage.setFlag(Flag.ENABLED);
        customMessageRepository.save(customMessage);
    }

    @Override
    public CustomMessageDTO findCustomMessage(Long customMessageId) throws CustomException {
        Optional<CustomMessage> customMessage = customMessageRepository.findById(customMessageId);
        if(customMessage.isPresent()) {
            return customMessageMapper.customMessageToCustomMessageDTO(customMessage.get());
        }else{
            throw new CustomException("Custom Message not found");
        }
    }

    @Override
    public List<CustomMessageDTO> findCustomMessagesByCompanyId(String companyId) throws CustomException {
        List<CustomMessageDTO> customMessages = new ArrayList<>();
        Company company = companyRepository.findCompanyByCompanyId(companyId);
        if(Objects.isNull(company)){
            throw new CustomException("Company Info not found");
        }

        List<CustomMessage> customMessageList = customMessageRepository.findAllByCompany(company);

        if(customMessageList.size() > 0){
            customMessageList.forEach(customMessage -> {
                CustomMessageDTO customMessageDTO = customMessageMapper.customMessageToCustomMessageDTO(customMessage);
                customMessageDTO.setCompanyId(customMessage.getCompany().getCompanyId());

                customMessages.add(customMessageDTO);
            });
        }
        return customMessages;
    }

    @Override
    public void update(CustomMessageDTO customMessageDTO) throws CustomException {
        Optional<CustomMessage> savedCustomMessage = customMessageRepository.findById(customMessageDTO.getId());
        if(savedCustomMessage.isPresent()) {
            validateUpdate(customMessageDTO, savedCustomMessage.get());
            CustomMessage customMessage = customMessageMapper.customMessageDTOToCustomMessage(customMessageDTO);
            Company company = companyRepository.findCompanyByCompanyId(customMessageDTO.getCompanyId());
            customMessage.setCompany(company);

            customMessageRepository.save(customMessage);
        }else{
            throw new CustomException("Custom Message not found");
        }
    }

    @Override
    public void delete(Long customMessageId) throws CustomException {
        Optional<CustomMessage> customMessage = customMessageRepository.findById(customMessageId);
        if(customMessage.isPresent()){
            customMessage.get().setFlag(Flag.DISABLED);
            customMessageRepository.save(customMessage.get());
        }else{
            throw new CustomException("Custom Message not found");
        }
    }


    private void validation(CustomMessageDTO customMessageDTO) throws CustomException {
        if(Objects.isNull(customMessageDTO.getSubject())){
            throw new CustomException("Subject is required");
        }

        if(Objects.isNull(customMessageDTO.getCompanyId())){
            throw new CustomException("Company Detail is required");
        }

        Company company = companyRepository.findCompanyByCompanyId(customMessageDTO.getCompanyId());
        if(Objects.isNull(company)){
            throw new CustomException("Invalid Company Id is required");
        }

        if(Objects.isNull(customMessageDTO.getMessage())){
            throw new CustomException("Message is required");
        }

        if(Objects.isNull(customMessageDTO.getStatus())){
            throw new CustomException("Status is required");
        }

        if(customMessageRepository.existsByCompanyAndStatus(company, customMessageDTO.getStatus())){
            throw new CustomException("Custom Message already exists for this application status");
        }
    }

    private void validateUpdate(CustomMessageDTO customMessageDTO, CustomMessage savedCustomMessage) throws CustomException {
        if(Objects.isNull(customMessageDTO.getCompanyId())){
            throw new CustomException("Company Detail is required");
        }

        Company company = companyRepository.findCompanyByCompanyId(customMessageDTO.getCompanyId());
        if(Objects.isNull(company)){
            throw new CustomException("Invalid Company Id is required");
        }

        if(!savedCustomMessage.getMessage().equalsIgnoreCase(customMessageDTO.getMessage())){
            if(Objects.isNull(customMessageDTO.getStatus())){
                throw new CustomException("Message is required");
            }
        }

        if(!savedCustomMessage.getStatus().equals(customMessageDTO.getStatus())){
            if(Objects.isNull(customMessageDTO.getMessage())){
                throw new CustomException("Status is required");
            }

            if(customMessageRepository.existsByCompanyAndStatus(company, customMessageDTO.getStatus())){
                throw new CustomException("Custom Message already exists for this application status");
            }
        }

        if(!savedCustomMessage.getSubject().equalsIgnoreCase(customMessageDTO.getSubject())){
            if(Objects.isNull(customMessageDTO.getSubject())){
                throw new CustomException("Subject is required");
            }
        }
    }
}
