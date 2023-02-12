package com.example.usermanagementsystem.controllers;

import com.example.usermanagementsystem.model.dtos.ResetPasswordDto;
import com.example.usermanagementsystem.model.entity.User;
import com.example.usermanagementsystem.model.dtos.CreateUserDto;
import com.example.usermanagementsystem.model.dtos.LoginUserDto;
import com.example.usermanagementsystem.model.response.UserResponse;
import com.example.usermanagementsystem.security.jwt.JwtTokenProvider;
import com.example.usermanagementsystem.service.interfaces.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/auth")
//@Validated
public class AuthController {
    private final Log logger = LogFactory.getLog(this.getClass());

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider){//, PermissionService permissionService, FileStorageServiceImpl fileStorageServiceImpl) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserDto createUserDto) {
        User user = userService.register(createUserDto);
        UserResponse userResponse = UserResponse.userResponseFromUser(user);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginUserDto loginUserDto){
        //todo move to the service
        try {
            String email = loginUserDto.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, loginUserDto.getPassword()));
            User user = userService.findByEmail(email);

            if (user.isPasswordResetRequired()){
                return new ResponseEntity<>("You need to reset your password", HttpStatus.UNAUTHORIZED);
            }

            String token = jwtTokenProvider.createToken(email, user.getRole());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", email);
            response.put("token", token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {

        userService.resetPassword(resetPasswordDto);
        return new ResponseEntity<>("Password successfully changed", HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
