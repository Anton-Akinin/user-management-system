package com.example.usermanagementsystem.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Setter
@Getter
public class AdminCreateUserDto {
    @NotBlank(message = "The first name is required")
    private String firstName;

    @NotBlank(message = "The last name is required")
    private String lastName;

    @Email(message = "Email address is not valid")
    @NotBlank(message = "The email address is required")
    private String email;
}
