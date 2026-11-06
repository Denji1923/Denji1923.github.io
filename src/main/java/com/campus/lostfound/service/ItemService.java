package com.campus.lostfound.service;

import com.campus.lostfound.dao.ItemDAO;
import com.campus.lostfound.exception.DuplicateRecordException;
import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.RecordNotFoundException;
import com.campus.lostfound.model.Admin;
import com.campus.lostfound.model.ApprovalStatus;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.ItemType;
import com.campus.lostfound.model.Student;
import com.campus.lostfound.util.AttachmentStore;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemService {
    /** Immutable List used by the Report Item drop-down. */
    public static final List<String> CATEGORIES = Collections.unmodifiableList(Arrays.asList(
            "Electronics", "Books & Notes", "ID / Cards", "Wallet / Money", "Bags",
            "Clothing", "Keys", "Accessories", "Other"));

    private final ItemDAO itemDAO = new ItemDAO();
    private final TransactionService transactions = new TransactionService();

    public Item reportItem(Student reporter, ItemType type, String name, String category,
                           String description, String location, LocalDate date) throws LostFoundException {
        return reportItem(reporter, type, name, category, description, location, date, null);   // overload: no picture
    }

    /** attachment = stored file name from AttachmentStore (or null). */
    public Item reportItem(Student reporter, ItemType type, String name, String category,
                           String description, String location, LocalDate date, String attachment) throws LostFoundException {
        Item item = Item.create(type, reporter.getId(), name, category, description, location, date); // validates
        item.setAttachment(attachment);

        // HashSet: quickly detect an identical report made by the same student
        Set<String> existingKeys = new HashSet<>();
        for (Item old : itemDAO.find(null, null, null, null, reporter.getId())) {
            existingKeys.add(key(old));
        }
        if (!existingKeys.add(key(item))) {
            throw new DuplicateRecordException("You already reported this exact item.");
        }

        itemDAO.save(item);
        transactions.record(reporter, "Report Item", type.getLabel() + " item: " + item.getName() + " (" + item.getLocation() + ")");
        return item;
    }

    private String key(Item i) {
        return (i.getType() + "|" + i.getName() + "|" + i.getLocation() + "|" + i.getDateOccurred()).toLowerCase();
    }

    // ----- overloaded search methods (Week 4) -----
    public List<Item> search(String keyword) throws LostFoundException {
        return search(keyword, null);
    }

    public List<Item> search(String keyword, ItemType type) throws LostFoundException {
        return itemDAO.find(ApprovalStatus.APPROVED, null, keyword, type, null);
    }

    public List<Item> getAvailableForClaim() throws LostFoundException {
        return itemDAO.find(ApprovalStatus.APPROVED, false, null, null, null);
    }

    public List<Item> getByReporter(Student student) throws LostFoundException {
        return itemDAO.find(null, null, null, null, student.getId());
    }

    /** filter == null means "all items". */
    public List<Item> getAll(ApprovalStatus filter) throws LostFoundException {
        return new ArrayList<>(itemDAO.find(filter, null, null, null, null));
    }

    public Item getById(int id) throws LostFoundException {
        return itemDAO.findById(id).orElseThrow(() -> new RecordNotFoundException("Item #" + id + " no longer exists."));
    }

    public void approve(Admin admin, int itemId) throws LostFoundException {
        Item item = getById(itemId);
        item.approve();
        itemDAO.updateApproval(itemId, item.getApproval());
        transactions.record(admin, "Approve Item", item.getName() + " marked Approved");
    }

    public void reject(Admin admin, int itemId) throws LostFoundException {
        Item item = getById(itemId);
        item.reject();
        itemDAO.updateApproval(itemId, item.getApproval());
        transactions.record(admin, "Reject Item", item.getName() + " marked Rejected");
    }

    public void delete(Admin admin, int itemId) throws LostFoundException {
        Item item = getById(itemId);
        itemDAO.delete(itemId);
        AttachmentStore.delete(item.getAttachment());
        transactions.record(admin, "Delete Item", "Deleted: " + item.getName());
    }
}
