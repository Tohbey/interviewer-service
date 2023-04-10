package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.CustomException;
import com.interview.interviewservice.entity.Company;
import com.interview.interviewservice.entity.Role;
import com.interview.interviewservice.entity.Team;
import com.interview.interviewservice.entity.User;
import com.interview.interviewservice.mapper.DTOS.TeamDTO;
import com.interview.interviewservice.mapper.DTOS.UserDTO;
import com.interview.interviewservice.mapper.mappers.RoleMapper;
import com.interview.interviewservice.mapper.mappers.TeamMapper;
import com.interview.interviewservice.mapper.mappers.UserMapper;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.repository.CompanyRepository;
import com.interview.interviewservice.repository.RoleRepository;
import com.interview.interviewservice.repository.TeamRepository;
import com.interview.interviewservice.repository.UserRepository;
import com.interview.interviewservice.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CompanyRepository companyRepository;

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, CompanyRepository companyRepository, RoleRepository roleRepository, RoleMapper roleMapper, TeamRepository teamRepository, TeamMapper teamMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    public void create(UserDTO userDTO) throws CustomException {
        validation(userDTO);
        User user = userMapper.userDTOToUser(userDTO);
        user.setFlag(Flag.ENABLED);

        Company company = companyRepository.findCompanyByCompanyId(userDTO.getCompanyId());
        Optional<Role> role = roleRepository.findById(userDTO.getRole().getId());
        user.setCompany(company);
        role.ifPresent(user::setRole);

        userRepository.save(user);
    }

    @Override
    public void delete(Long userId) throws Exception {
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isPresent()){
            user.get().setFlag(Flag.DISABLED);
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
        Company company = companyRepository.findCompanyByCompanyId(userDTO.getCompanyId());
        Optional<Role> role = roleRepository.findById(userDTO.getRole().getId());
        user.setCompany(company);
        role.ifPresent(user::setRole);
        Optional<Team> team = teamRepository.findById(userDTO.getTeamDTO().getId());
        team.ifPresent(user::setTeam);

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

        if(!savedUser.getTeam().getName()
                .equalsIgnoreCase(userDTO.getTeamDTO().getName())){
            if (!teamRepository.existsByNameAndCompany(userDTO.getTeamDTO().getName(), company)){
                throw new CustomException("Team Does Not Exist");
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

        if (!teamRepository.existsByNameAndCompany(user.getTeamDTO().getName(), company)){
            throw new CustomException("Team Does Not Exist");
        }
    }
}
