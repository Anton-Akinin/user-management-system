package com.example.usermanagementsystem.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UpdateUserDto {
    private String firstName;

    private String lastName;

    private Date birthDate;

    private String gender;
}
