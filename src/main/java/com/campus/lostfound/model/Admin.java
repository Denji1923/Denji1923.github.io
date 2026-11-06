package com.campus.lostfound.model;

import com.campus.lostfound.util.PasswordUtil;
import com.campus.lostfound.util.Validator;

/** An administrator logs in with username + password. The password hash stays PRIVATE (encapsulation). */
public class Admin extends User {
    private String username;
    private final String passwordHash;
    private final String salt;

    public Admin(int id, String username, String fullName, String passwordHash, String salt) {
        super(id, fullName);
        this.username = Validator.requireText(username, "Username", 50);
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public Admin(String username, String fullName, String passwordHash, String salt) {
        this(0, username, fullName, passwordHash, salt);
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getSalt() { return salt; }

    public boolean verifyPassword(String password) {
        return password != null && PasswordUtil.verify(password, salt, passwordHash);
    }

    @Override public String getRole() { return "Administrator"; }
    @Override public String getIdentifier() { return username; }
}
