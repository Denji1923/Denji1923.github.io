package com.campus.lostfound.exception;

public class AuthenticationException extends LostFoundException {
    public AuthenticationException(String message) { super(message); }
    public AuthenticationException(String message, Throwable cause) { super(message, cause); }
}
