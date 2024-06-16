package com.albertcode.emailnotification.service;

import com.albertcode.emailnotification.constants.UserConstants;
import com.albertcode.emailnotification.exception.ResourceNotFoundException;
import com.albertcode.emailnotification.exception.UserAlreadyExistsException;
import com.albertcode.emailnotification.repository.UserRepository;
import com.albertcode.emailnotification.entity.User;
import com.albertcode.emailnotification.mapper.UserMapper;
import com.albertcode.emailnotification.payload.EmailDetails;
import com.albertcode.emailnotification.payload.RequestDto;
import com.albertcode.emailnotification.payload.UserDetailsDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailServiceImpl emailService;
    @Override
    public void registerUser(RequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.getEmail()))
            throw new UserAlreadyExistsException(UserConstants.USER_ALREADY_EXISTS);
        User user = UserMapper.mapToUser(new User(),requestDto);
//        user.setPassword(""); // Define a senha como uma string vazia
        emailService.sendEmail(EmailDetails.builder()
                        .messageBody("You’ve successfully subscribed! with mail id: "+requestDto.getEmail() + "Stay tuned for updates and exclusive content.")
                        .recipient(requestDto.getEmail())
                        .subject("REGISTRATION SUCCESS")
                .build());
        userRepository.save(user);
    }

    @Override
    public UserDetailsDto getUserByEmail(String email) {
        if(!userRepository.existsByEmail(email))
            throw new ResourceNotFoundException(UserConstants.USER_NOT_FOUND);
        User user = userRepository.findByEmail(email);
        return UserMapper.mapToUserDetails(new UserDetailsDto(),user);
    }

    @Override
    public List<UserDetailsDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty())
            throw new ResourceNotFoundException(UserConstants.USER_NOT_FOUND);
        List<UserDetailsDto> userDetailsDtos = new ArrayList<>();
        users.forEach(user -> userDetailsDtos.add(UserMapper.mapToUserDetails(new UserDetailsDto(),user)));
        return userDetailsDtos;
    }

    @Transactional
    @Override
    public boolean updateUser(RequestDto requestDto) {
        if(!userRepository.existsByEmail(requestDto.getEmail()))
            throw new ResourceNotFoundException(UserConstants.USER_NOT_FOUND);
        User user = userRepository.findByEmail(requestDto.getEmail());
        User updatedUser = UserMapper.mapToUser(user,requestDto);
        userRepository.save(updatedUser);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteUser(String email) {
        if(!userRepository.existsByEmail(email))
            throw new ResourceNotFoundException(UserConstants.USER_NOT_FOUND);
        userRepository.deleteByEmail(email);
        return true;
    }
}
