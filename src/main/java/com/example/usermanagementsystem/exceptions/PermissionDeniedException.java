package com.example.usermanagementsystem.exceptions;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String message){
        super(message);
    }
}
