package com.campus.lostfound.service;

import com.campus.lostfound.dao.ClaimDAO;
import com.campus.lostfound.dao.ItemDAO;
import com.campus.lostfound.exception.DuplicateRecordException;
import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.RecordNotFoundException;
import com.campus.lostfound.exception.ValidationException;
import com.campus.lostfound.model.Admin;
import com.campus.lostfound.model.Claim;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.Student;
import java.util.List;

public class ClaimService {
    private final ClaimDAO claimDAO = new ClaimDAO();
    private final ItemDAO itemDAO = new ItemDAO();
    private final TransactionService transactions = new TransactionService();

    public Claim submitClaim(Student student, int itemId, String proof) throws LostFoundException {
        Item item = itemDAO.findById(itemId)
                .orElseThrow(() -> new RecordNotFoundException("That item no longer exists."));
        if (!item.isApproved() || item.isClaimed()) {
            throw new ValidationException("That item is not available for claiming.");
        }
        if (claimDAO.existsPending(itemId, student.getId())) {
            throw new DuplicateRecordException("You already have a pending claim for this item.");
        }
        Claim claim = claimDAO.save(new Claim(itemId, student.getId(), proof));      // validates the proof
        transactions.record(student, "Claim Item", "Claimed: " + item.getName());
        return claim;
    }

    public List<Claim> getAll() throws LostFoundException { return claimDAO.findAll(); }

    public List<Claim> getByStudent(Student student) throws LostFoundException {
        return claimDAO.findByStudent(student.getId());
    }

    public void approve(Admin admin, int claimId) throws LostFoundException {
        Claim claim = claimDAO.findById(claimId).orElseThrow(() -> new RecordNotFoundException("Claim not found."));
        claim.approve();
        claimDAO.updateStatus(claimId, claim.getApproval());
        itemDAO.markClaimed(claim.getItemId());
        claimDAO.rejectOtherPending(claim.getItemId(), claimId);
        transactions.record(admin, "Approve Claim", claim.getItemName() + " - claim by " + claim.getStudentName() + " approved");
    }

    public void reject(Admin admin, int claimId) throws LostFoundException {
        Claim claim = claimDAO.findById(claimId).orElseThrow(() -> new RecordNotFoundException("Claim not found."));
        claim.reject();
        claimDAO.updateStatus(claimId, claim.getApproval());
        transactions.record(admin, "Reject Claim", claim.getItemName() + " - claim by " + claim.getStudentName() + " rejected");
    }
}
