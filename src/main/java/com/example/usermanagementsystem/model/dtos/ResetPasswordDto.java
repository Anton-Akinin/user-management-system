package com.example.usermanagementsystem.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPasswordDto {
    @NotBlank(message = "This field is required")
    private String email;

    @NotBlank(message = "This field is required")
    private String oldPassword;

    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String newPassword;

    @Size(min = 6, message = "Must be at least 6 characters")
    @NotBlank(message = "This field is required")
    private String confirmNewPassword;
}
