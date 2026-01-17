package com.temporaldb.cli.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * History manager for CLI.
 */
public class HistoryManager {
    private static final Logger logger = LoggerFactory.getLogger(HistoryManager.class);

    private final List<String> history = new ArrayList<>();
    private final int maxSize;

    public HistoryManager(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Add command to history.
     */
    public void add(String command) {
        if (!command.trim().isEmpty()) {
            history.add(command);

            // Keep history size limited
            if (history.size() > maxSize) {
                history.remove(0);
            }
        }
    }

    /**
     * Get command at index.
     */
    public String get(int index) {
        if (index >= 0 && index < history.size()) {
            return history.get(index);
        }
        return null;
    }

    /**
     * Get all history.
     */
    public List<String> getAll() {
        return new ArrayList<>(history);
    }

    /**
     * Get history size.
     */
    public int size() {
        return history.size();
    }

    /**
     * Clear history.
     */
    public void clear() {
        history.clear();
    }
}
