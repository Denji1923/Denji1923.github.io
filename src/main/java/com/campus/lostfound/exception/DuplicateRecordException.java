package com.campus.lostfound.exception;

public class DuplicateRecordException extends LostFoundException {
    public DuplicateRecordException(String message) { super(message); }
    public DuplicateRecordException(String message, Throwable cause) { super(message, cause); }
}
