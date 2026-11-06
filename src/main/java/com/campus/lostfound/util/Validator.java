package com.campus.lostfound.util;

import com.campus.lostfound.exception.ValidationException;
import java.time.LocalDate;
import java.util.regex.Pattern;

/** Static helper methods (Week 4) with method OVERLOADING. Every method throws ValidationException on bad data. */
public final class Validator {
    private static final Pattern STUDENT_NUMBER = Pattern.compile("^\\d{2}-\\d{2}-\\d{3}$");
    private static final int MIN_PASSWORD_LENGTH = 8;

    private Validator() { }

    public static String requireText(String value, String field) {
        return requireText(value, field, 255);          // overload -> default max length
    }

    public static String requireText(String value, String field, int maxLength) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(field + " is required.");
        }
        String cleaned = value.trim().replaceAll("\\s+", " ");
        if (cleaned.length() > maxLength) {
            throw new ValidationException(field + " must be at most " + maxLength + " characters.");
        }
        return cleaned;
    }

    public static String optionalText(String value, int maxLength) {
        if (value == null) return "";
        String cleaned = value.trim().replaceAll("\\s+", " ");
        if (cleaned.length() > maxLength) {
            throw new ValidationException("Text must be at most " + maxLength + " characters.");
        }
        return cleaned;
    }

    public static String requireStudentNumber(String value) {
        String text = requireText(value, "Student number", 9);
        if (!STUDENT_NUMBER.matcher(text).matches()) {
            throw new ValidationException("Student number must look like 25-22-081.");
        }
        return text;
    }

    public static String requireFullName(String value) {
        String text = requireText(value, "Full name", 120);
        if (text.length() < 3) throw new ValidationException("Please enter your full name.");
        return text;
    }

    public static String requirePassword(String value) {
        if (value == null || value.length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        return value;
    }

    public static LocalDate requirePastOrToday(LocalDate date, String field) {
        if (date == null) throw new ValidationException(field + " is required.");
        if (date.isAfter(LocalDate.now())) throw new ValidationException(field + " cannot be in the future.");
        return date;
    }
}
