package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.*;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.mapper.mappers.RoleMapper;
import com.interview.interviewservice.mapper.mappers.TeamMapper;
import com.interview.interviewservice.mapper.mappers.UserMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.*;
import com.interview.interviewservice.service.UserContextService;
import com.interview.interviewservice.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CompanyRepository companyRepository;

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    private final TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserContextService userContextService;


    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl .class);


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, CompanyRepository companyRepository, RoleRepository roleRepository, RoleMapper roleMapper, TeamRepository teamRepository, TeamMapper teamMapper, TokenRepository tokenRepository, UserContextService userContextService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.tokenRepository = tokenRepository;
        this.userContextService = userContextService;
    }

    @Override
    @Transactional
    public void create(UserDTO userDTO) throws CustomException {
        validation(userDTO);
        String password = RandomStringUtils.randomAlphabetic(10);
        logger.info("Password: {}",password);

        String encryptedPassword = passwordEncoder.encode(password);

        User user = userMapper.userDTOToUser(userDTO);
        user.setFlag(Flag.ENABLED);
        user.setPassword(encryptedPassword);
        user.setIsNewUser(true);

        Company company = companyRepository.findCompanyByCompanyId(userDTO.getCompanyId());
        Optional<Role> role = roleRepository.findById(userDTO.getRole().getId());
        user.setCompany(company);
        role.ifPresent(user::setRole);

        User savedUser = userRepository.save(user);

        Token verifyToken = new Token();
        String token = RandomStringUtils.randomAlphabetic(40);


        //adding 20 minutes to the current time
        Calendar present = Calendar.getInstance();
        long timeInSecs = present.getTimeInMillis();
        Date expiredAt = new Date(timeInSecs + (20 * 60 * 1000));

        verifyToken.setToken(token);
        verifyToken.setUser(savedUser);
        verifyToken.setExpiredAt(expiredAt);

        Token savedToken = tokenRepository.save(verifyToken);
        userCreationEmail(savedUser, savedToken);
    }

    @Override
    public void delete(Long userId) throws Exception {
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isPresent()){
            user.get().setFlag(Flag.DISABLED);
            user.get().setLastModifiedDate(new Date());
            user.get().setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());
            userRepository.save(user.get());
        }else{
            throw new CustomException("User Not found");
        }
    }

    @Override
    public UserDTO find(Long userId) throws CustomException {
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isPresent()){
            return mapper(user.get());
        }else{
            throw new CustomException("User Not found");
        }
    }

    @Override
    public UserDTO mapper(User user){
        UserDTO userDTO = userMapper.userToUserDTO(user);
        userDTO.setCompanyId(user.getCompany().getCompanyId());
        userDTO.setRole(roleMapper.roleToRoleDTO(user.getRole()));
        userDTO.setFullname(user.getUserFullName());
        TeamDTO teamDTO = teamMapper.teamToTeamDTO(user.getTeam());
        userDTO.setTeamDTO(teamDTO);

        return userDTO;
    }

    @Override
    public List<UserDTO> findUsersBy(Long companyId, Flag flag) throws CustomException {
        List<UserDTO> userDTOS = new ArrayList<>();
        Optional<Company> company = companyRepository.findById(companyId);
        if(company.isEmpty()){
            throw new CustomException("Company detail not found");
        }

        List<User> users = userRepository.findAllByCompanyAndFlag(company.get(), flag);

        users.forEach(user -> {
            UserDTO userDTO = mapper(user);

            userDTOS.add(userDTO);
        });

        return userDTOS;
    }

    @Override
    public void update(UserDTO userDTO) throws CustomException {
        Optional<User> savedUser = userRepository.findById(userDTO.getId());
        if(savedUser.isEmpty()){
            throw new CustomException("User Doesnt Exist");
        }

        validateUpdate(userDTO, savedUser.get());
        User user = userMapper.userDTOToUser(userDTO);

        user.setPassword(savedUser.get().getPassword());
        user.setIsNewUser(savedUser.get().getIsNewUser());
        user.setIsActive(savedUser.get().getIsActive());
        user.setFlag(savedUser.get().getFlag());
        user.setLastModifiedDate(new Date());
        user.setLastModifiedBy(userContextService.getCurrentUserDTO().getFullname());

        Company company = companyRepository.findCompanyByCompanyId(userDTO.getCompanyId());
        Optional<Role> role = roleRepository.findById(userDTO.getRole().getId());
        user.setCompany(company);
        role.ifPresent(user::setRole);

        if(Objects.nonNull(userDTO.getTeamDTO())){
            Optional<Team> team = teamRepository.findById(userDTO.getTeamDTO().getId());
            team.ifPresent(user::setTeam);
        }

        userRepository.save(user);
    }

    private void validateUpdate(UserDTO userDTO, User savedUser) throws CustomException {
        Company company = companyRepository.findCompanyByCompanyId(userDTO.getCompanyId());

        if(!savedUser.getEmail().equalsIgnoreCase(userDTO.getEmail())){
            if(userRepository.existsByEmailIgnoreCase(userDTO.getEmail())){
                throw new CustomException("Email Already Exist");
            }
        }

        if(!savedUser.getPhoneNumber().equalsIgnoreCase(userDTO.getPhoneNumber())){
            if(userRepository.existsByPhoneNumberIgnoreCase(userDTO.getPhoneNumber())){
                throw new CustomException("Phone Number Already Exist");
            }
        }

        if(!savedUser.getRole().getRoleName().name()
                .equalsIgnoreCase(userDTO.getRole().getRoleName().name())){
            if(roleRepository.findById(userDTO.getRole().getId()).isEmpty()){
                throw new CustomException("Role Does Not Exist");
            }
        }

        if(!savedUser.getCompany().getCompanyId()
                .equalsIgnoreCase(userDTO.getCompanyId())){
            if(Objects.isNull(company)){
                throw new CustomException("Company Details not found");
            }
        }

        if(Objects.nonNull(userDTO.getTeamDTO()) && Objects.nonNull(savedUser.getTeam())){
            if(!savedUser.getTeam().getName()
                    .equalsIgnoreCase(userDTO.getTeamDTO().getName())){
                if (!teamRepository.existsByNameAndCompany(userDTO.getTeamDTO().getName(), company)){
                    throw new CustomException("Team Does Not Exist");
                }
            }
        }
    }

    private void validation(UserDTO user) throws CustomException {
        Company company = companyRepository.findCompanyByCompanyId(user.getCompanyId());

        if(userRepository.existsByEmailIgnoreCase(user.getEmail())){
            throw new CustomException("Email Already Exist");
        }

        if(userRepository.existsByPhoneNumberIgnoreCase(user.getPhoneNumber())){
            throw new CustomException("Phone Number Already Exist");
        }

        if(!companyRepository.existsByCompanyIdIgnoreCase(user.getCompanyId())){
            throw new CustomException("Associated Company Does Not Exist");
        }

        if(roleRepository.findById(user.getRole().getId()).isEmpty()){
            throw new CustomException("Role Does Not Exist");
        }

        if(Objects.nonNull(user.getTeamDTO())){
            if (!teamRepository.existsByNameAndCompany(user.getTeamDTO().getName(), company)){
                throw new CustomException("Team Does Not Exist");
            }
        }
    }

    private void userCreationEmail(User user, Token token){

    }
}
