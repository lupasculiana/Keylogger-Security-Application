package com.example.processor.dto;

import com.example.processor.model.RoleEnum;
import com.example.processor.model.User;

public class UserMapper {
    public static UserDTO mapToCustomerDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                fromEnumToString(user.getRole())
        );
    }

    public static User mapToCustomer(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getEmail(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                fromStringToEnum(userDTO.getRole())
        );
    }

    public static String fromEnumToString(RoleEnum roleEnum){
        if(roleEnum.equals(RoleEnum.ADMIN)){
            return "ADMIN";
        }else{
            return "USER";
        }
    }

    public static RoleEnum fromStringToEnum(String role){
        if(role.equals("ADMIN")){
            return RoleEnum.ADMIN;
        }else{
            return RoleEnum.USER;
        }
    }
}

