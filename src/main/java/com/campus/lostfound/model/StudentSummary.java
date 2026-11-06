package com.campus.lostfound.model;

/** One row of the admin Students page: a student plus how many reports and claims they made. */
public class StudentSummary {
    private final Student student;
    private final int reports;
    private final int claims;

    public StudentSummary(Student student, int reports, int claims) {
        this.student = student;
        this.reports = reports;
        this.claims = claims;
    }

    public Student getStudent() { return student; }
    public String getStudentNumber() { return student.getStudentNumber(); }
    public String getFullName() { return student.getFullName(); }
    public int getReports() { return reports; }
    public int getClaims() { return claims; }
}
