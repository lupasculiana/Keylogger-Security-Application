package com.example.processor.service;


import com.example.processor.config.JwtService;
import com.example.processor.dto.UserDTO;
import com.example.processor.dto.UserMapper;
import com.example.processor.model.User;
import com.example.processor.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setRole("USER");
        User user = UserMapper.mapToCustomer(userDTO);
        userRepository.save(user);
        return UserMapper.mapToCustomerDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(UserMapper::mapToCustomerDto).toList();
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();
        return UserMapper.mapToCustomerDto(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException(
                    "user with id " + userId + " does not exist!");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).get();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDTO checkForUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (existingUser.getPassword().equals(userDTO.getPassword())) {
                return UserMapper.mapToCustomerDto(existingUser);
            } else {
                throw new IllegalArgumentException("Invalid credentials");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public String getUsernameByToken(String token) {
        JwtService jwtService = new JwtService();
        jwtService.extractUsername(token);
        return jwtService.extractUsername(token);
    }
}
