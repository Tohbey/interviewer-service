package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Job;
import com.interview.interviewservice.entity.Stage;
import com.interview.interviewservice.mapper.DTOS.JobDTO;
import com.interview.interviewservice.mapper.DTOS.StageDTO;
import com.interview.interviewservice.mapper.mappers.JobMapper;
import com.interview.interviewservice.mapper.mappers.StageMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.JobRepository;
import com.interview.interviewservice.repository.StageRepository;
import com.interview.interviewservice.service.JobService;
import com.interview.interviewservice.service.UserContextService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    private final JobMapper jobMapper;

    private final CompanyRepository companyRepository;

    private final StageRepository stageRepository;

    private final StageMapper  stageMapper;

    private final UserContextService userContextService;


    public JobServiceImpl(JobRepository jobRepository,
                          JobMapper jobMapper,
                          CompanyRepository companyRepository,
                          StageRepository stageRepository,
                          StageMapper stageMapper,
                          UserContextService userContextService) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.companyRepository = companyRepository;
        this.stageRepository = stageRepository;
        this.stageMapper = stageMapper;
        this.userContextService = userContextService;
    }

    @Override
    @Transactional
    public void create(JobDTO jobDTO) throws CustomException {
        jobDTO.setJobId(RandomStringUtils.randomAlphanumeric(15));
        validate(jobDTO);
        Job job = mapper(jobDTO);

        Company company = companyRepository.findCompanyByCompanyId(jobDTO.getCompanyId());
        if(Objects.isNull(company)){
            throw  new CustomException("Company not found");
        }

        job.setCompany(company);
        job.setFlag(Flag.ENABLED);
        List<Stage> stages = new ArrayList<>();
        for(StageDTO stageDTO: jobDTO.getStages()){
            Optional<Stage> stageOptional = stageRepository.findByIdAndCompany(stageDTO.getId(), company);
            if(stageOptional.isPresent()){
                stages.add(stageOptional.get());
            }else{
                throw new CustomException("Stages not found");
            }
        }
        job.setStages(stages);

        jobRepository.save(job);
    }

    @Override
    public JobDTO find(Long jobId) throws CustomException {
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isPresent()) {
            return jobDtoMapper(job.get());
        }else{
            throw new CustomException("Job Details not found");
        }
    }

    @Override
    public JobDTO jobDtoMapper(Job job) {
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);
        jobDTO.setCompanyId(job.getCompany().getCompanyId());
        jobDTO.setDeadLine(job.getDeadline());

        return jobDTO;
    }


    @Override
    public void update(JobDTO jobDTO) throws CustomException {
        Optional<Job> savedJob = jobRepository.findById(jobDTO.getId());
        if (savedJob.isPresent()) {
            validateUpdate(jobDTO, savedJob.get());
            Job job = mapper(jobDTO);

            job.setFlag(savedJob.get().getFlag());
            List<Stage> stages = new ArrayList<>();
            jobDTO.getStages().forEach(stageDTO -> {
                Optional<Stage> stageOptional = stageRepository.findById(stageDTO.getId());
                stages.add(stageOptional.get());
            });
            Company company = companyRepository.findCompanyByCompanyId(jobDTO.getCompanyId());
            job.setCompany(company);
            job.setStages(stages);
            jobRepository.save(job);
        }else{
            throw new CustomException("Job Details not found");
        }
    }

    @Override
    public void delete(Long jobId) throws CustomException {
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isPresent()) {
            job.get().setFlag(Flag.DISABLED);
            job.get().setLastModifiedDate(new Date());
            job.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
            jobRepository.save(job.get());
        }else{
            throw new CustomException("Job Details not found");
        }
    }

    @Override
    public List<JobDTO> findJobsByCompany(String companyId, Flag flag) throws CustomException {
        List<JobDTO> jobDTOS = new ArrayList<>();
        Company company = companyRepository.findCompanyByCompanyId(companyId);
        if (Objects.nonNull(company)) {
            List<Job> jobs = new ArrayList<>();
            if(Objects.nonNull(flag)){
                jobs = jobRepository.findAllByCompanyAndFlag(company, flag);
            }else{
                jobs = jobRepository.findAllByCompany(company);
            }
            jobs.forEach(job -> {
                JobDTO jobDTO = jobMapper.jobToJobDTO(job);
                jobDTO.setCompanyId(job.getCompany().getCompanyId());
                jobDTO.setDeadLine(job.getDeadline());
                jobDTOS.add(jobDTO);
            });

            return jobDTOS;
        }else{
            throw new CustomException("Company Details not found");
        }
    }

    private void validate(JobDTO jobDTO) throws CustomException {
        Company company = companyRepository.findCompanyByCompanyId(jobDTO.getCompanyId());

        if(Objects.isNull(company)){
            throw new CustomException("Company Details not found");
        }

        Set<StageDTO> stageSet = new HashSet<>(jobDTO.getStages());

        if (jobDTO.getStages().size() != stageSet.size()) {
            throw new CustomException("Duplicate Stages dedicated");
        }

        jobDTO.getStages().forEach(stageDTO -> {
            Optional<Stage> stageOptional = stageRepository.findById(stageDTO.getId());
            if(stageOptional.isEmpty()){
                try {
                    throw new CustomException("Stage Details not found");
                } catch (CustomException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Optional<Job> job = jobRepository.findByJobId(jobDTO.getJobId());
        if(job.isPresent()){
            jobDTO.setJobId("#".concat(jobDTO.getCompanyId().substring(0, 2))+RandomStringUtils.randomAlphanumeric(13));
        }

    }

    private void validateUpdate(JobDTO jobDTO, Job savedJob) throws CustomException {
        Company company = companyRepository.findCompanyByCompanyId(jobDTO.getCompanyId());

        if(Objects.isNull(company)){
            throw new CustomException("Company Details not found");
        }

        if(!savedJob.getDescription().equalsIgnoreCase(jobDTO.getDescription())){
            if(jobRepository.existsByTitleAndCompany(jobDTO.getTitle(), company)){
                throw new CustomException("Job Title Already Exist");
            }
        }

        Set<StageDTO> stageSet = new HashSet<>(jobDTO.getStages());

        if (jobDTO.getStages().size() != stageSet.size()) {
            throw new CustomException("Duplicate Stages dedicated");
        }

        jobDTO.getStages().forEach(stageDTO -> {
            Optional<Stage> stageOptional = stageRepository.findById(stageDTO.getId());
            if(stageOptional.isEmpty()){
                try {
                    throw new CustomException("Stage Details not found");
                } catch (CustomException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Job mapper(JobDTO jobDTO) throws CustomException {
        Job job = jobMapper.jobDTOToJob(jobDTO);
        if(Objects.isNull(job.getId())){
            job.setCreatedDate(new Date());
            job.setCreatedBy(userContextService.getCurrentUserDTO().getFullname());
        }else{
            job.setLastModifiedDate(new Date());
            job.setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
        }

        return job;
    }
}
