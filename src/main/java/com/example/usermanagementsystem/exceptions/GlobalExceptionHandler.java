package com.example.usermanagementsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailException.class)
    public ExceptionData exceptionDuringRegistration(EmailException exception) {
        log.error("EmailException: ", exception);
        return new ExceptionData(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordException.class)
    public ExceptionData passwordException(PasswordException exception) {
        log.error("PasswordException: ", exception);
        return new ExceptionData(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ExceptionData userNotFoundException(UserNotFoundException exception) {
        log.error("UserNotFoundException: ", exception);
        return new ExceptionData(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionDeniedException.class)
    public ExceptionData permissionDeniedException(PermissionDeniedException exception) {
        log.error("PermissionDeniedException: ", exception);
        return new ExceptionData(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ExceptionData badCredentialsException(BadCredentialsException exception) {
        log.error("BadCredentialsException: ", exception);
        return new ExceptionData(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionData globalExceptionHandler(Exception exception) {
        log.error("Exception: ", exception);
        return new ExceptionData(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @Data
    @AllArgsConstructor
    private static class ExceptionData {
        String exceptionType;
        String exceptionMessage;
    }
}
