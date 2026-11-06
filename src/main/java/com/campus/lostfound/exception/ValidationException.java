package com.campus.lostfound.exception;

/** UNCHECKED exception thrown by setters/constructors when data is invalid (like IllegalArgumentException in Week 13). */
public class ValidationException extends IllegalArgumentException {
    public ValidationException(String message) { super(message); }
}
