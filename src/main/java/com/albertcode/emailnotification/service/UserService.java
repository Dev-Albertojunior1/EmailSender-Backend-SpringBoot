package com.albertcode.emailnotification.service;

import com.albertcode.emailnotification.payload.RequestDto;
import com.albertcode.emailnotification.payload.UserDetailsDto;

import java.util.List;

public interface UserService {

    public void registerUser(RequestDto requestDto);

    public UserDetailsDto getUserByEmail(String email);

    public List<UserDetailsDto> getAllUsers();

    public boolean updateUser(RequestDto requestDto);

    public boolean deleteUser(String email);
}
