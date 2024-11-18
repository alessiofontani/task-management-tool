package com.alessiofontani.taskmanagementtool.exception;

public class InvalidLoginPasswordException extends RuntimeException {
    public InvalidLoginPasswordException(String message) {
        super(message);
    }
}
