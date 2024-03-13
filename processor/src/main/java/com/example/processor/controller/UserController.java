package com.example.processor.controller;


import com.example.processor.dto.UserDTO;
import com.example.processor.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin("http://localhost:3000/")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        userDTO.setRole("USER");
        log.info("new user registration {}", userDTO);
        UserDTO loggedInUser = userService.createUser(userDTO);
        return new ResponseEntity<>(loggedInUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @CrossOrigin("http://localhost:3000/")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO loggedInUser = userService.checkForUser(userDTO);
            return ResponseEntity.ok(loggedInUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserById(@PathVariable("id") Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUser(@PathVariable("id") Integer userId,
                               @RequestBody UserDTO userDTO) {
        userDTO.setId(userId);
        userService.updateUser(userDTO);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable("id") Integer userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    public String getUserIdByToken(@RequestHeader("Authorization") String token) {
        System.out.printf("token: %s\n", token);
        String username = userService.getUsernameByToken(token);
        System.out.printf(username + "\n" );
        return username;
    }

}
