package com.temporaldb.core.security;

import java.util.*;
import lombok.Data;

/**
 * Represents a database user with roles and permissions.
 */
@Data
public class User {
    private final String userId;
    private final String username;
    private final String passwordHash;
    private final String email;
    private final Set<String> roles;
    private final boolean active;
    private final long createdAt;
    private long lastLogin;

    public User(String userId, String username, String passwordHash, String email) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.roles = new HashSet<>();
        this.active = true;
        this.createdAt = System.currentTimeMillis();
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public void removeRole(String role) {
        roles.remove(role);
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public void recordLogin() {
        this.lastLogin = System.currentTimeMillis();
    }
}