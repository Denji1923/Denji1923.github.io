package com.campus.lostfound.model;

import java.time.LocalDate;

public class LostItem extends Item {
    public LostItem(int id, int reporterId, String name, String category, String description,
                    String location, LocalDate date, ApprovalStatus approval, boolean claimed) {
        super(id, reporterId, name, category, description, location, date, approval, claimed);
    }

    public LostItem(int reporterId, String name, String category, String description,
                    String location, LocalDate date) {
        super(reporterId, name, category, description, location, date);
    }

    @Override public ItemType getType() { return ItemType.LOST; }
    @Override public String describeEvent() { return "Lost at " + getLocation() + " on " + getDateOccurred(); }
}
