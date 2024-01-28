package com.interview.interviewservice.service;


import com.interview.interviewservice.Util.Constant;
import com.interview.interviewservice.elastic.*;
import com.interview.interviewservice.entity.*;
import com.interview.interviewservice.repository.*;
import com.interview.interviewservice.repository.eRepository.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;
@Service
public class ElasticSynchronizer {

    @Autowired
    private IUserERepo iUserERepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IJobERepo iJobERepo;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private IStageERepo iStageERepo;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private ITeamERepo iTeamERepo;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ICompanyERepo iCompanyERepo;

    @Autowired
    private CompanyRepository companyRepository;

    private static final Logger logger = LoggerFactory.getLogger(ElasticSynchronizer.class);

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void sync(){
        logger.info("Start Syncing - {}", LocalDateTime.now());
        syncUser();
        syncJob();
        syncTeam();
        syncStage();
        syncCompany();
        logger.info("End Syncing - {}", LocalDateTime.now());
    }

    private void syncUser(){
        Specification<User> userSpecification = (root, query, criteriaBuilder) -> getModificationDatePredicate(criteriaBuilder, root);

        List<User> userList;

        if(iUserERepo.count() == 0){
            userList = userRepository.findAll();
        }else{
            userList = userRepository.findAll(userSpecification);
        }

        for(User user: userList){
            int i = 0;
            UserModel userModel = new UserModel(user.getId(), user.getSurname(), user.getOtherNames(), user.getCompany().getCompanyId(),"");
            logger.info("Syncing Users - {}", i);
            iUserERepo.save(userModel);
            i+=1;
        }
    }

    private void syncJob(){
        Specification<Job> jobSpecification = (root, query, criteriaBuilder) -> getModificationDatePredicate(criteriaBuilder, root);

        List<Job> jobs;

        if(iJobERepo.count() == 0){
            jobs = jobRepository.findAll();
        }else{
            jobs = jobRepository.findAll(jobSpecification);
        }

        for(Job job: jobs){
            int i = 0;
            JobModel jobModel = new JobModel(job.getId(), job.getTitle(), job.getJobId(),
                    job.getSection(), job.getLocation(), job.getCountry(), job.getCompany().getCompanyId());
            logger.info("Syncing Job - {}", i);
            iJobERepo.save(jobModel);
            i+=1;
        }
    }

    private void syncStage(){
        Specification<Stage> stageSpecification = (root, query, criteriaBuilder) -> getModificationDatePredicate(criteriaBuilder, root);

        List<Stage> stages;

        if(iStageERepo.count() == 0){
            stages = stageRepository.findAll();
        }else{
            stages = stageRepository.findAll(stageSpecification);
        }

        for(Stage stage: stages){
            int i = 0;
            StageModel stageModel = new StageModel(stage.getId(), stage.getDescription(), stage.getCompany().getCompanyId());
            logger.info("Syncing Stage - {}", i);
            iStageERepo.save(stageModel);
            i+=1;
        }
    }

    private void syncTeam(){
        Specification<Team> teamSpecification = (root, query, criteriaBuilder) -> getModificationDatePredicate(criteriaBuilder, root);

        List<Team> teams;

        if(iTeamERepo.count() == 0){
            teams = teamRepository.findAll();
        }else{
            teams = teamRepository.findAll(teamSpecification);
        }

        for(Team team: teams){
            int i = 0;
            TeamModel teamModel = new TeamModel(team.getId(), team.getName(), team.getSection(), team.getCompany().getCompanyId());
            logger.info("Syncing Team - {}", i);
            iTeamERepo.save(teamModel);
            i+=1;
        }
    }

    private void syncCompany(){
        Specification<Company> companySpecification = (root, query, criteriaBuilder) -> getModificationDatePredicate(criteriaBuilder, root);

        List<Company> companies;

        if(iCompanyERepo.count() == 0){
            companies = companyRepository.findAll();
        }else{
            companies = companyRepository.findAll(companySpecification);
        }

        for(Company company: companies){
            int i = 0;
            CompanyModel companyModel = new CompanyModel(company.getId(), company.getCompanyName(), company.getCountry());
            logger.info("Syncing Company - {}", i);
            iCompanyERepo.save(companyModel);
            i+=1;
        }
    }

    private static Predicate getModificationDatePredicate(CriteriaBuilder cb, Root<?> root) {
        Expression<Timestamp> currentTime;
        currentTime = cb.currentTimestamp();

        Expression<Timestamp> currentTimeMinus = cb.literal(
                new Timestamp(System.currentTimeMillis() - (Constant.INTERVAL_IN_MILLISECONDE)));

        return cb.between(root.<Date>get(Constant.MODIFICATION_DATE),
                currentTimeMinus,
                currentTime
        );
    }
}
