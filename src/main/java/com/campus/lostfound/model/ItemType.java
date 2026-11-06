package com.campus.lostfound.model;

public enum ItemType {
    LOST("Lost"), FOUND("Found");

    private final String label;
    ItemType(String label) { this.label = label; }
    public String getLabel() { return label; }
    public String getCssClass() { return name().toLowerCase(); }
    @Override public String toString() { return label; }
}
