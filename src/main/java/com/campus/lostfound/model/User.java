package com.campus.lostfound.model;

import com.campus.lostfound.util.Validator;

/** ABSTRACT superclass (Week 10). Subclasses must say what role they play and how they are identified. */
public abstract class User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String fullName;

    protected User(int id, String fullName) {
        this.id = id;
        setFullName(fullName);
    }

    protected User(String fullName) {              // constructor overloading + this(...) chaining (Week 7)
        this(0, fullName);
    }

    public int getId() { return id; }
    public final void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public final void setFullName(String fullName) {
        this.fullName = Validator.requireFullName(fullName);
    }

    public abstract String getRole();          // polymorphism: each subclass answers differently
    public abstract String getIdentifier();    // student number OR username

    @Override
    public String toString() { return getRole() + ": " + fullName + " (" + getIdentifier() + ")"; }
}
