package com.campus.lostfound.model;

import com.campus.lostfound.util.Validator;
import java.time.LocalDate;

/**
 * ABSTRACT class (Week 10) implementing two INTERFACES (Week 11).
 * LostItem and FoundItem extend it (Week 8) and override the abstract methods (Week 9 polymorphism).
 * All fields are PRIVATE; setters validate (Week 7 encapsulation).
 */
public abstract class Item implements Approvable, Exportable {
    private int id;
    private int reporterId;
    private String reporterName = "";
    private String name;
    private String category;
    private String description;
    private String location;
    private LocalDate dateOccurred;
    private ApprovalStatus approval;
    private boolean claimed;
    private String attachment;          // stored file name of the attached picture, or null

    protected Item(int id, int reporterId, String name, String category, String description,
                   String location, LocalDate dateOccurred, ApprovalStatus approval, boolean claimed) {
        this.id = id;
        this.reporterId = reporterId;
        setName(name);
        setCategory(category);
        setDescription(description);
        setLocation(location);
        setDateOccurred(dateOccurred);
        this.approval = approval;
        this.claimed = claimed;
    }

    /** Constructor for a brand-new report: always starts PENDING and not claimed. */
    protected Item(int reporterId, String name, String category, String description,
                   String location, LocalDate dateOccurred) {
        this(0, reporterId, name, category, description, location, dateOccurred, ApprovalStatus.PENDING, false);
    }

    /** Factory method: picks the right subclass for the type. */
    public static Item create(ItemType type, int id, int reporterId, String name, String category,
                              String description, String location, LocalDate date,
                              ApprovalStatus approval, boolean claimed) {
        switch (type) {
            case LOST:  return new LostItem(id, reporterId, name, category, description, location, date, approval, claimed);
            case FOUND: return new FoundItem(id, reporterId, name, category, description, location, date, approval, claimed);
            default:    throw new IllegalArgumentException("Unknown item type: " + type);
        }
    }

    public static Item create(ItemType type, int reporterId, String name, String category,
                              String description, String location, LocalDate date) {
        return create(type, 0, reporterId, name, category, description, location, date, ApprovalStatus.PENDING, false);
    }

    // ----- abstract behaviour: every subclass must provide it -----
    public abstract ItemType getType();
    public abstract String describeEvent();

    // ----- getters -----
    public int getId() { return id; }
    public int getReporterId() { return reporterId; }
    public String getReporterName() { return reporterName; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public LocalDate getDateOccurred() { return dateOccurred; }
    public boolean isClaimed() { return claimed; }
    public String getAttachment() { return attachment; }
    public boolean isHasAttachment() { return attachment != null && !attachment.isEmpty(); }
    public String getAvailability() { return claimed ? "Claimed" : "Available"; }
    @Override public ApprovalStatus getApproval() { return approval; }

    // ----- setters with validation -----
    public final void setId(int id) { this.id = id; }
    public final void setReporterName(String reporterName) { this.reporterName = reporterName == null ? "" : reporterName; }
    public final void setName(String name) { this.name = Validator.requireText(name, "Item name", 120); }
    public final void setCategory(String category) { this.category = Validator.requireText(category, "Category", 60); }
    public final void setDescription(String description) { this.description = Validator.optionalText(description, 1000); }
    public final void setLocation(String location) { this.location = Validator.requireText(location, "Location", 150); }
    public final void setDateOccurred(LocalDate date) { this.dateOccurred = Validator.requirePastOrToday(date, "Date"); }
    public final void setClaimed(boolean claimed) { this.claimed = claimed; }
    public final void setAttachment(String attachment) { this.attachment = (attachment == null || attachment.isEmpty()) ? null : attachment; }

    // ----- Approvable -----
    @Override public void approve() { this.approval = ApprovalStatus.APPROVED; }
    @Override public void reject()  { this.approval = ApprovalStatus.REJECTED; }

    // ----- Exportable -----
    @Override public String[] csvHeader() {
        return new String[] {"ID", "Type", "Item", "Category", "Location", "Date", "Reported By", "Approval", "Availability"};
    }
    @Override public String[] csvFields() {
        return new String[] {String.valueOf(id), getType().getLabel(), name, category, location,
                dateOccurred.toString(), reporterName, approval.getLabel(), getAvailability()};
    }

    @Override public String toString() { return getType().getLabel() + ": " + name; }
}
