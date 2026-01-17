package com.temporaldb.core.temporal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a point-in-time snapshot of the database.
 */
public class TemporalSlice {
    private static final Logger logger = LoggerFactory.getLogger(TemporalSlice.class);

    private final long timestamp;
    private final String name;
    private final Map<String, List<Object>> tableData = new ConcurrentHashMap<>();
    private final long createdAt;

    public TemporalSlice(long timestamp, String name) {
        this.timestamp = timestamp;
        this.name = name;
        this.createdAt = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public void addTableSnapshot(String tableName, List<Object> data) {
        tableData.put(tableName, new ArrayList<>(data));
    }

    public List<Object> getTableSnapshot(String tableName) {
        return tableData.get(tableName);
    }

    public Set<String> getTables() {
        return tableData.keySet();
    }

    public long getSize() {
        return tableData.values().stream()
                .mapToLong(List::size)
                .sum();
    }
}
