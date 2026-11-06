package com.campus.lostfound.model;

import java.time.LocalDate;

public class FoundItem extends Item {
    public FoundItem(int id, int reporterId, String name, String category, String description,
                     String location, LocalDate date, ApprovalStatus approval, boolean claimed) {
        super(id, reporterId, name, category, description, location, date, approval, claimed);
    }

    public FoundItem(int reporterId, String name, String category, String description,
                     String location, LocalDate date) {
        super(reporterId, name, category, description, location, date);
    }

    @Override public ItemType getType() { return ItemType.FOUND; }
    @Override public String describeEvent() { return "Found at " + getLocation() + " on " + getDateOccurred(); }
}
