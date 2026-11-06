package com.campus.lostfound.model;

import java.time.LocalDateTime;

/** One row of the audit trail ("transaction") shown to administrators. */
public class TransactionRecord implements Exportable {
    private int id;
    private final String actorType;
    private final String actorName;
    private final String action;
    private final String details;
    private final LocalDateTime createdAt;

    public TransactionRecord(int id, String actorType, String actorName, String action, String details, LocalDateTime createdAt) {
        this.id = id;
        this.actorType = actorType;
        this.actorName = actorName;
        this.action = action;
        this.details = details;
        this.createdAt = createdAt;
    }

    public TransactionRecord(String actorType, String actorName, String action, String details) {
        this(0, actorType, actorName, action, details, LocalDateTime.now());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getActorType() { return actorType; }
    public String getActorName() { return actorName; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedAtText() { return com.campus.lostfound.util.Formats.time(createdAt); }

    @Override public String[] csvHeader() { return new String[] {"ID", "Time", "User", "Role", "Action", "Details"}; }
    @Override public String[] csvFields() {
        return new String[] {String.valueOf(id), String.valueOf(createdAt).replace('T', ' '), actorName, actorType, action, details};
    }
}
