package com.example.usermanagementsystem.controllers;

import com.example.usermanagementsystem.model.dtos.UpdateUserDto;
import com.example.usermanagementsystem.model.entity.User;
import com.example.usermanagementsystem.model.response.UserResponse;
import com.example.usermanagementsystem.security.jwt.JwtUser;
import com.example.usermanagementsystem.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long userId = jwtUser.getId();
        User user = userService.findById(userId);
        UserResponse userResponse = UserResponse.userResponseFromUser(user);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        UserResponse userResponse = UserResponse.userResponseFromUser(user);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("userId") Long userId,
                                                   @Valid @RequestBody UpdateUserDto updateUserDto) {
        User user = userService.updateUser(userId, updateUserDto);
        UserResponse userResponse = UserResponse.userResponseFromUser(user);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> delete(@PathVariable Long userId) {
        userService.delete(userId);

        return new ResponseEntity<>("User deleted", HttpStatus.OK);

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers(@RequestParam(required = false) List<String> firstName,
                               @RequestParam(required = false) List<String> lastName,
                               @RequestParam(required = false) List<Long> userIds,
                               @RequestParam(required = false) String role,
                               Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String currentUserEmail = jwtUser.getEmail();
        List<User> users = userService.getUsers(firstName, lastName, userIds, role, currentUserEmail);

        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::userResponseFromUser)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }


}

