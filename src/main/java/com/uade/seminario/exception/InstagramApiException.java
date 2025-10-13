package com.uade.seminario.exception;

public class InstagramApiException extends RuntimeException {
    
    public InstagramApiException(String message) {
        super(message);
    }
    
    public InstagramApiException(String message, Throwable cause) {
        super(message, cause);
    }
}

