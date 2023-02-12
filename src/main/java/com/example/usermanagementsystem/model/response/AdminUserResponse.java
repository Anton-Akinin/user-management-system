package com.example.usermanagementsystem.model.response;

import com.example.usermanagementsystem.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private Date birthDate;
    private String gender;

    public static AdminUserResponse userResponseFromUser(User user){
        AdminUserResponse adminUserResponse = new AdminUserResponse();
        adminUserResponse.setId(user.getId());
        adminUserResponse.setFirstName(user.getFirstName());
        adminUserResponse.setLastName(user.getLastName());
        adminUserResponse.setEmail(user.getEmail());
        adminUserResponse.setBirthDate(user.getBirthDate());
        adminUserResponse.setGender(user.getGender());
        adminUserResponse.setPassword(adminUserResponse.getPassword());
        adminUserResponse.setRole(user.getRole().getName());

        return adminUserResponse;
    }
}
