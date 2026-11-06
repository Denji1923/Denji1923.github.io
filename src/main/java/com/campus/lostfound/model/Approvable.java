package com.campus.lostfound.model;

/** Week 11 INTERFACE: anything an administrator can approve or reject. */
public interface Approvable {
    void approve();
    void reject();
    ApprovalStatus getApproval();

    default boolean isApproved() { return getApproval() == ApprovalStatus.APPROVED; }
    default boolean isPending()  { return getApproval() == ApprovalStatus.PENDING; }
}
