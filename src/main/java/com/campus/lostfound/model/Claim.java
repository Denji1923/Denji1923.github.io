package com.campus.lostfound.model;

import com.campus.lostfound.util.Validator;
import java.time.LocalDateTime;

public class Claim implements Approvable, Exportable {
    private int id;
    private final int itemId;
    private final int studentId;
    private String itemName = "";
    private String studentName = "";
    private String studentNumber = "";
    private String proof;
    private ApprovalStatus approval;
    private LocalDateTime createdAt;

    public Claim(int id, int itemId, int studentId, String proof, ApprovalStatus approval, LocalDateTime createdAt) {
        this.id = id;
        this.itemId = itemId;
        this.studentId = studentId;
        setProof(proof);
        this.approval = approval;
        this.createdAt = createdAt;
    }

    public Claim(int itemId, int studentId, String proof) {
        this(0, itemId, studentId, proof, ApprovalStatus.PENDING, LocalDateTime.now());
    }

    public int getId() { return id; }
    public final void setId(int id) { this.id = id; }
    public int getItemId() { return itemId; }
    public int getStudentId() { return studentId; }
    public String getItemName() { return itemName; }
    public String getStudentName() { return studentName; }
    public String getStudentNumber() { return studentNumber; }
    public String getProof() { return proof; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedAtText() { return com.campus.lostfound.util.Formats.time(createdAt); }
    @Override public ApprovalStatus getApproval() { return approval; }

    public final void setProof(String proof) {
        String cleaned = Validator.requireText(proof, "Proof of ownership", 1000);
        if (cleaned.length() < 10) {
            throw new com.campus.lostfound.exception.ValidationException("Please give more detail (at least 10 characters) as proof.");
        }
        this.proof = cleaned;
    }

    public void setDisplayInfo(String itemName, String studentName, String studentNumber) {
        this.itemName = itemName;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
    }

    @Override public void approve() { this.approval = ApprovalStatus.APPROVED; }
    @Override public void reject()  { this.approval = ApprovalStatus.REJECTED; }

    @Override public String[] csvHeader() {
        return new String[] {"ID", "Item", "Claimed By", "Student No.", "Proof", "Status", "Submitted"};
    }
    @Override public String[] csvFields() {
        return new String[] {String.valueOf(id), itemName, studentName, studentNumber, proof, approval.getLabel(), String.valueOf(createdAt)};
    }
}
