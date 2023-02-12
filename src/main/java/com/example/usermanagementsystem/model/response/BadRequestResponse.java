package com.example.usermanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
public class BadRequestResponse {
    private Map<String, String> data;
}
