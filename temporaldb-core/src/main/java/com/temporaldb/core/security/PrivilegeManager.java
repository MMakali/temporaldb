package com.temporaldb.core.security;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages role-based access control and permissions.
 */
public class PrivilegeManager {
    private static final Logger logger = LoggerFactory.getLogger(PrivilegeManager.class);

    private final Map<String, Role> roles = new ConcurrentHashMap<>();

    public PrivilegeManager() {
        // Initialize built-in roles
        roles.put("ADMIN", Role.admin());
        roles.put("TRANSACTION_PROCESSOR", Role.transactionProcessor());
        roles.put("ANALYST", Role.analyst());
    }

    /**
     * Check if user has permission.
     * @param user User to check
     * @param permission Permission to verify
     * @return true if user has permission
     */
    public boolean checkPermission(User user, String permission) {
        for (String roleName : user.getRoles()) {
            Role role = roles.get(roleName);
            if (role != null && role.hasPermission(permission)) {
                logger.debug("Permission granted: {} for user {}", permission, user.getUsername());
                return true;
            }
        }

        logger.warn("Permission denied: {} for user {}", permission, user.getUsername());
        return false;
    }

    /**
     * Grant role to user.
     * @param user User
     * @param roleName Role name
     */
    public void grantRole(User user, String roleName) {
        if (!roles.containsKey(roleName)) {
            throw new IllegalArgumentException("Unknown role: " + roleName);
        }
        user.addRole(roleName);
        logger.info("Role {} granted to user {}", roleName, user.getUsername());
    }

    /**
     * Revoke role from user.
     * @param user User
     * @param roleName Role name
     */
    public void revokeRole(User user, String roleName) {
        user.removeRole(roleName);
        logger.info("Role {} revoked from user {}", roleName, user.getUsername());
    }
}