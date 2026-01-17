package com.temporaldb.cli.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Session manager for CLI.
 */
public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private String currentUser;
    private String authToken;
    private long sessionStartTime;
    private Map<String, Object> sessionData = new HashMap<>();

    /**
     * Create session.
     */
    public void createSession(String username, String token) {
        this.currentUser = username;
        this.authToken = token;
        this.sessionStartTime = System.currentTimeMillis();
        logger.info("Session created for user: {}", username);
    }

    /**
     * End session.
     */
    public void endSession() {
        this.currentUser = null;
        this.authToken = null;
        logger.info("Session ended");
    }

    /**
     * Check if session is active.
     */
    public boolean isActive() {
        return currentUser != null && authToken != null;
    }

    /**
     * Get current user.
     */
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * Get auth token.
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Get session duration.
     */
    public long getSessionDuration() {
        if (isActive()) {
            return System.currentTimeMillis() - sessionStartTime;
        }
        return 0;
    }

    /**
     * Store data in session.
     */
    public void putData(String key, Object value) {
        sessionData.put(key, value);
    }

    /**
     * Get data from session.
     */
    public Object getData(String key) {
        return sessionData.get(key);
    }
}
