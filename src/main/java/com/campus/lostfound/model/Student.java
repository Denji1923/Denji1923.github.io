package com.campus.lostfound.model;

import com.campus.lostfound.util.Validator;

/** A student logs in with a student number + full name (no password). */
public class Student extends User {
    private String studentNumber;

    public Student(int id, String studentNumber, String fullName) {
        super(id, fullName);                        // super(...) - Week 8
        setStudentNumber(studentNumber);
    }

    public Student(String studentNumber, String fullName) {
        super(fullName);
        setStudentNumber(studentNumber);
    }

    public String getStudentNumber() { return studentNumber; }
    public final void setStudentNumber(String studentNumber) {
        this.studentNumber = Validator.requireStudentNumber(studentNumber);
    }

    @Override public String getRole() { return "Student"; }                 // @Override - Week 8
    @Override public String getIdentifier() { return studentNumber; }
}
