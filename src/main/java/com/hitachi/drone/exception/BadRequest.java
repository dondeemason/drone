package com.hitachi.drone.exception;

public class BadRequest extends RuntimeException {
    public BadRequest (String message) {
        super(message);
    };
}
