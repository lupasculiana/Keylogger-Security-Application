package com.example.processor.service;

import com.example.processor.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Integer userId);

    void deleteUser(Integer userId);

    void updateUser(UserDTO userDTO);

    UserDTO checkForUser(UserDTO userDTO);

    String getUsernameByToken(String token);
}
