package com.hitachi.drone.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String resource, String field, Object value) {
        super(resource + " not found with " + field + " " + value);
    }

    public NotFoundException(String message){
        super(message);
    }
}
