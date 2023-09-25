package com.interview.interviewservice;

import com.interview.interviewservice.entity.Role;
import com.interview.interviewservice.model.RoleEnum;
import com.interview.interviewservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class InterviewServiceApplication implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(InterviewServiceApplication.class, args);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Role> rolesList = roleRepository.findAll();

        if(rolesList.isEmpty()){
            Role role = new Role(1L, RoleEnum.ADMIN_USER);
            Role role1 = new Role(2L, RoleEnum.USER);
            Role role2 = new Role(3L, RoleEnum.CANDIDATE);

            List<Role> roles = new ArrayList<>();
            roles.add(role);
            roles.add(role1);
            roles.add(role2);

            roleRepository.saveAll(roles);
        }
    }
}
