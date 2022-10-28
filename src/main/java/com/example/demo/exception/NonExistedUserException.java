package com.example.demo.exception;

public class NonExistedUserException extends RuntimeException{
    public NonExistedUserException(String message) {
        super(message);
    }
}
