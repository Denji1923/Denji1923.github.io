package com.campus.lostfound.exception;

public class DatabaseException extends LostFoundException {
    public DatabaseException(String message) { super(message); }
    public DatabaseException(String message, Throwable cause) { super(message, cause); }
}
