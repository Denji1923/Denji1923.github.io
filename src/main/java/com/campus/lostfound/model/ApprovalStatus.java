package com.campus.lostfound.model;

public enum ApprovalStatus {
    PENDING("Pending"), APPROVED("Approved"), REJECTED("Rejected");

    private final String label;
    ApprovalStatus(String label) { this.label = label; }
    public String getLabel() { return label; }

    /** Used by the web pages to pick a badge colour, e.g. "badge-pending". */
    public String getCssClass() { return name().toLowerCase(); }

    // Simple yes/no getters so JSP pages can write ${item.approval.pending}
    public boolean isPending()  { return this == PENDING; }
    public boolean isApproved() { return this == APPROVED; }
    public boolean isRejected() { return this == REJECTED; }

    @Override public String toString() { return label; }
}
