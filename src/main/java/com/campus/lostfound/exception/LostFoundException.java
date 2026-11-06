package com.campus.lostfound.exception;

/** Base CHECKED exception for the application (Week 13: custom exceptions). */
public class LostFoundException extends Exception {
    public LostFoundException(String message) { super(message); }
    public LostFoundException(String message, Throwable cause) { super(message, cause); }
}
