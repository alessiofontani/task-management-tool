package com.alessiofontani.taskmanagementtool.controller;

import com.alessiofontani.taskmanagementtool.enums.ErrorCode;
import com.alessiofontani.taskmanagementtool.exception.*;
import com.alessiofontani.taskmanagementtool.payload.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(ErrorCode.GENERIC, "An unexpected error occurred.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ErrorCode.USER_NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidLoginPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidLoginPasswordException ex) {
        ErrorResponse error = new ErrorResponse(ErrorCode.INVALID_PASSWORD, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(ErrorCode.EMAIL_ALREADY_EXISTS, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(ErrorCode.USERNAME_ALREADY_EXISTS, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordComplexityException.class)
    public ResponseEntity<ErrorResponse> handlePasswordComplexityException(PasswordComplexityException ex) {
        ErrorResponse error = new ErrorResponse(ErrorCode.PASSWORD_COMPLEXITY_ERROR, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatchException(PasswordMismatchException ex) {
        ErrorResponse error = new ErrorResponse(ErrorCode.PASSWORD_MISMATCH_ERROR, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
