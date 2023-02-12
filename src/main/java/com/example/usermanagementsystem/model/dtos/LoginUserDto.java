package com.example.usermanagementsystem.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class LoginUserDto {
    @Email(message = "Email address is not valid")
    @NotBlank(message = "The email address is required")
    private String email;

    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "The password is required")
    private String password;
}
