package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.controller.AuthenticationController;
import com.interview.interviewservice.entity.*;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.mapper.mappers.RoleMapper;
import com.interview.interviewservice.mapper.mappers.TeamMapper;
import com.interview.interviewservice.mapper.mappers.UserMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.*;
import com.interview.interviewservice.service.MailSender;
import com.interview.interviewservice.service.UserContextService;
import com.interview.interviewservice.service.UserService;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private MailSender mailSender;

    @Value("${base.url}")
    private String baseUrl;

    private final UserContextService userContextService;


    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl .class);


    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           CompanyRepository companyRepository,
                           RoleRepository roleRepository,
                           RoleMapper roleMapper,
                           TeamRepository teamRepository,
                           TeamMapper teamMapper,
                           TokenRepository tokenRepository,
                           UserContextService userContextService) {
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
    public void create(UserDTO userDTO) throws CustomException, MessagingException {
        validation(userDTO);
        String password = RandomStringUtils.randomAlphabetic(10);
        logger.info("Password: {}",password);

        String encryptedPassword = passwordEncoder.encode(password);

        User user = userMapper.userDTOToUser(userDTO);
        user.setFlag(Flag.ENABLED);
        user.setPassword(encryptedPassword);
        user.setIsNewUser(true);

        if(Objects.nonNull(userDTO.getCompanyId())){
            Company company = companyRepository.findCompanyByCompanyId(userDTO.getCompanyId());
            user.setCompany(company);
        }

        if(Objects.nonNull(userDTO.getRole())){
            Optional<Role> role = roleRepository.findById(userDTO.getRole().getId());
            role.ifPresent(user::setRole);
        }

        User savedUser = userRepository.save(user);

        Token savedToken = generateToken(savedUser);

        verificationEmail(savedUser, savedToken);
        userCreationEmail(savedUser, password);
    }

    @Override
    public void delete(Long userId) throws CustomException {
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
        Set<TeamDTO> teamDTOs = new HashSet<TeamDTO>();
        if(user.getTeams().size() > 0){
            user.getTeams().forEach(team -> {
                teamDTOs.add(teamMapper.teamToTeamDTO(team));
            });
        }
        userDTO.setTeamDTO(teamDTOs);

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
            userDTO.getTeamDTO().forEach(teamDTO -> {
                Optional<Team> team = teamRepository.findById(teamDTO.getId());
                team.ifPresent(value -> user.getTeams().add(value));
            });
        }

        userRepository.save(user);
    }

    @Override
    public List<UserDTO> findUsersByTeam(Team team) {
        List<UserDTO> userDTOS = new ArrayList<>();

        List<User> users = userRepository.findUserByTeamsContains(team);
        users.forEach(user -> {
            userDTOS.add(mapper(user));
        });

        return userDTOS;
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

        if(Objects.nonNull(userDTO.getTeamDTO()) && Objects.nonNull(savedUser.getTeams())){
            if(savedUser.getTeams().size() != userDTO.getTeamDTO().size()){
                userDTO.getTeamDTO().forEach(teamDTO -> {
                    if (!teamRepository.existsByNameAndCompany(teamDTO.getName(), company)){
                        try {
                            throw new CustomException("Team Does Not Exist");
                        } catch (CustomException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
    }

    private void validation(UserDTO user) throws CustomException {
        if(Objects.nonNull(user.getCompanyId())){
            if(!companyRepository.existsByCompanyIdIgnoreCase(user.getCompanyId())){
                throw new CustomException("Associated Company Does Not Exist");
            }
        }

        if(userRepository.existsByEmailIgnoreCase(user.getEmail())){
            throw new CustomException("Email Already Exist");
        }

        if(userRepository.existsByPhoneNumberIgnoreCase(user.getPhoneNumber())){
            throw new CustomException("Phone Number Already Exist");
        }

        if(roleRepository.findById(user.getRole().getId()).isEmpty()){
            throw new CustomException("Role Does Not Exist");
        }
    }

    private void verificationEmail(User user, Token token) throws MessagingException {
        HashMap<String, String> data = new HashMap<>();

        data.put("userDescription", user.getUserFullName());
        data.put("verificationLink", baseUrl+AuthenticationController.BASE_URL+"verify?email="+user.getEmail()+"&token="+token.getToken());

        mailSender.send(user.getEmail(), "Email Address Verification" ,data, "email-verification.html");
    }

    private void userCreationEmail(User user, String password) throws MessagingException {
        HashMap<String, String> data = new HashMap<>();

        if(Objects.nonNull(user.getCompany())){
            data.put("companyName", user.getCompany().getCompanyName());
        }else{
            data.put("companyName", "Admin");
        }
        data.put("userDescription", user.getUserFullName());
        data.put("userRole", user.getRole().getRoleName().name());
        data.put("username", user.getEmail());
        data.put("password", password);

        mailSender.send(user.getEmail(), "User Creation" ,data, "user-creation.html");
    }

    private Token generateToken(User savedUser){
        Token verifyToken = new Token();
        String token = RandomStringUtils.randomAlphabetic(40);

        //adding 20 minutes to the current time
        Calendar present = Calendar.getInstance();
        long timeInSecs = present.getTimeInMillis();
        Date expiredAt = new Date(timeInSecs + (20 * 60 * 1000));

        verifyToken.setToken(token);
        verifyToken.setUser(savedUser);
        verifyToken.setExpiredAt(expiredAt);

        return tokenRepository.save(verifyToken);
    }
}
