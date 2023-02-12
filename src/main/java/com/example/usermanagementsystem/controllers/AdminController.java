package com.example.usermanagementsystem.controllers;

import com.example.usermanagementsystem.events.OnCreateUserEvent;
import com.example.usermanagementsystem.model.dtos.AdminCreateUserDto;
import com.example.usermanagementsystem.model.dtos.UpdateUserDto;
import com.example.usermanagementsystem.model.entity.User;
import com.example.usermanagementsystem.model.response.AdminUserResponse;
import com.example.usermanagementsystem.service.interfaces.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    public AdminController(UserService userService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @ApiOperation(value = "Endpoint for creating user by admin")
    @PostMapping("/user")
    public ResponseEntity<AdminUserResponse> createUser(@Valid @RequestBody AdminCreateUserDto adminCreateUserDto) {
        User user = userService.createUser(adminCreateUserDto);

        eventPublisher.publishEvent(new OnCreateUserEvent(user));
        AdminUserResponse adminUserResponse = AdminUserResponse.userResponseFromUser(user);

        return new ResponseEntity<>(adminUserResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Endpoint to update user by admin")
    @PutMapping("/user/{userId}")
    public ResponseEntity<AdminUserResponse> updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody UpdateUserDto updateUserDto) {
        User user = userService.updateUser(userId, updateUserDto);

        AdminUserResponse adminUserResponse = AdminUserResponse.userResponseFromUser(user);
        return new ResponseEntity<>(adminUserResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Endpoint to delete user by admin")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @ApiOperation(value = "Endpoint to grand to user admin role")
    @PutMapping("/user/{userId}/grandAdminRole")
    public ResponseEntity<Object> grandAdminRole(@PathVariable("userId") Long userId) {
        userService.assignAdminRoleToUser(userId);
        return new ResponseEntity<>("Admin role granted", HttpStatus.OK);
    }

}

