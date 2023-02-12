package com.example.usermanagementsystem.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
public class CreateUserDto {
    @NotBlank(message = "The first name is required")
    private String firstName;

    @NotBlank(message = "The last name is required")
    private String lastName;

    @Email(message = "Email address is not valid")
    @NotBlank(message = "The email address is required")
    private String email;

    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "The password is required")
    private String password;

    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String confirmPassword;

    private Date birthDate;

    private String gender;
}
