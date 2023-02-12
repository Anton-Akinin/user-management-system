package com.example.usermanagementsystem.service.interfaces;

import com.example.usermanagementsystem.model.dtos.AdminCreateUserDto;
import com.example.usermanagementsystem.model.dtos.ResetPasswordDto;
import com.example.usermanagementsystem.model.entity.User;
import com.example.usermanagementsystem.model.dtos.CreateUserDto;
import com.example.usermanagementsystem.model.dtos.UpdateUserDto;

import java.util.List;

public interface UserService {

    User createUser(AdminCreateUserDto adminCreateUserDto);

    User register(CreateUserDto createUserDto);

    List<User> getAll();

    User findById(Long id);

    void delete(Long id);

    User findByEmail(String email);

    User updateUser(Long id, UpdateUserDto updateUserDto);

    void resetPassword(ResetPasswordDto resetPasswordDto);

    List<User> getUsers(List<String> firstNames,
                        List<String> lastNames,
                        List<Long> userIds,
                        String roles,
                        String currentUserEmail);

    void assignAdminRoleToUser(Long id);
}
