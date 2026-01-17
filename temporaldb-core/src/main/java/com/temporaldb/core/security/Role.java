package com.temporaldb.core.security;

import lombok.Getter;

import java.util.*;

/**
 * Represents a role with permissions.
 */
public class Role {
    @Getter
    private final String name;
    @Getter
    private final String description;
    private final Set<String> permissions;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
        this.permissions = new HashSet<>();
    }

    public void grantPermission(String permission) {
        permissions.add(permission);
    }

    public void revokePermission(String permission) {
        permissions.remove(permission);
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public Set<String> getPermissions() { return new HashSet<>(permissions); }

    // Built-in roles
    public static Role admin() {
        Role admin = new Role("ADMIN", "Administrator with all permissions");
        admin.grantPermission("READ");
        admin.grantPermission("WRITE");
        admin.grantPermission("DELETE");
        admin.grantPermission("HARD_DELETE");
        admin.grantPermission("USER_MANAGEMENT");
        return admin;
    }

    public static Role transactionProcessor() {
        Role proc = new Role("TRANSACTION_PROCESSOR", "Can insert and update transactions");
        proc.grantPermission("READ");
        proc.grantPermission("WRITE");
        proc.grantPermission("DELETE");
        return proc;
    }

    public static Role analyst() {
        Role analyst = new Role("ANALYST", "Read-only access");
        analyst.grantPermission("READ");
        return analyst;
    }
}