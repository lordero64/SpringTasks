package com.egorov.springtasks.exception;

public class DuplicateIsbnException extends RuntimeException{
    public DuplicateIsbnException(String message){
        super(message);
    }
}
