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
import com.interview.interviewservice.service.AuthenticationService;
import com.interview.interviewservice.service.JobService;
import com.interview.interviewservice.service.UserContextService;
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
    public void create(JobDTO jobDTO) throws CustomException {
        validate(jobDTO);
        Job job = mapper(jobDTO);

        Company company = companyRepository.findCompanyByCompanyId(jobDTO.getCompanyId());
        job.setCompany(company);
        job.setFlag(Flag.ENABLED);
        List<Stage> stages = new ArrayList<>();
        jobDTO.getStages().forEach(stageDTO -> {
            Optional<Stage> stageOptional = stageRepository.findById(stageDTO.getId());
            stages.add(stageOptional.get());
        });
        job.setStages(stages);

        jobRepository.save(job);
    }

    @Override
    public JobDTO find(Long jobId) throws CustomException {
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isPresent()) {
            JobDTO jobDTO = jobMapper.jobToJobDTO(job.get());
            job.get().getStages().forEach(stage -> {
                StageDTO stageDTO = stageMapper.stageToStageDTO(stage);
                jobDTO.getStages().add(stageDTO);
            });
            jobDTO.setCompanyId(job.get().getCompany().getCompanyId());

            return jobDTO;
        }else{
            throw new CustomException("Job Details not found");
        }
    }

    @Override
    public void update(JobDTO jobDTO) throws CustomException {
        Optional<Job> savedJob = jobRepository.findById(jobDTO.getId());
        if (savedJob.isPresent()) {
            validateUpdate(jobDTO, savedJob.get());
            Job job = mapper(jobDTO);

            List<Stage> stages = new ArrayList<>();
            jobDTO.getStages().forEach(stageDTO -> {
                Optional<Stage> stageOptional = stageRepository.findById(stageDTO.getId());
                stages.add(stageOptional.get());
            });
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
    public List<JobDTO> findJobsByCompany(Long companyId, Flag flag) throws CustomException {
        List<JobDTO> jobDTOS = new ArrayList<>();
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            List<Job> jobs = jobRepository.findAllByCompanyAndFlag(company.get(), flag);
            jobs.forEach(job -> {
                JobDTO jobDTO = jobMapper.jobToJobDTO(job);
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

        if(jobRepository.existsByTitleAndCompany(jobDTO.getTitle(), company)){
            throw new CustomException("Job Title Already Exist");
        }

        jobDTO.getStages().forEach(stageDTO -> {
//          trying to prevent duplicate stages.
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

        jobDTO.getStages().forEach(stageDTO -> {
//          trying to prevent duplicate stages.
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
