package com.campus.lostfound.service;

import com.campus.lostfound.dao.TransactionDAO;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.TransactionRecord;
import com.campus.lostfound.model.User;
import com.campus.lostfound.util.AppLogger;
import java.util.List;

/** Records every action to the database (admin "Transactions" page) and to logs/activity.log. */
public class TransactionService {
    private final TransactionDAO dao = new TransactionDAO();

    public void record(User actor, String action, String details) {
        String type = actor.getRole().equals("Administrator") ? "admin" : "student";
        record(type, actor.getFullName(), action, details);
    }

    public void record(String actorType, String actorName, String action, String details) {
        AppLogger.log(actorType + " " + actorName + " | " + action + " | " + details);
        try {
            dao.save(new TransactionRecord(actorType, actorName, action, details));
        } catch (DatabaseException e) {
            AppLogger.log("FAILED to save transaction: " + e.getMessage());   // logging must never crash the app
        }
    }

    public List<TransactionRecord> latest(int limit) throws DatabaseException { return dao.latest(limit); }

    public List<TransactionRecord> search(String keyword, String actorType) throws DatabaseException {
        return dao.search(keyword, actorType, 500);
    }
}
