package com.campus.lostfound.exception;

public class RecordNotFoundException extends LostFoundException {
    public RecordNotFoundException(String message) { super(message); }
    public RecordNotFoundException(String message, Throwable cause) { super(message, cause); }
}
