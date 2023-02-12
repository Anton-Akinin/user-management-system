package com.example.usermanagementsystem.model.response;

import com.example.usermanagementsystem.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class UserListResponse {
    private List<User> data;
}
