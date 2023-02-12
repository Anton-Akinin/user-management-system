package com.example.usermanagementsystem.security;

import com.example.usermanagementsystem.model.entity.User;
import com.example.usermanagementsystem.security.jwt.JwtUser;
import com.example.usermanagementsystem.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    private final UserService userService;

    @Autowired
    public UserSecurity(UserService userService) {
        this.userService = userService;
    }

    public boolean checkUserId(Authentication authentication, Long userId) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String email = jwtUser.getEmail();
        User user = userService.findByEmail(email);

        return user.getId().equals(userId);
    }
}
