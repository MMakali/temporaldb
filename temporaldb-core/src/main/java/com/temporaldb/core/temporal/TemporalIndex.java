package com.temporaldb.core.temporal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Index for temporal queries.
 * Supports efficient time-based lookups.
 */
public class TemporalIndex {
    private static final Logger logger = LoggerFactory.getLogger(TemporalIndex.class);

    private final String name;
    private final NavigableMap<Long, List<Long>> timeToRowIds = new TreeMap<>();

    public TemporalIndex(String name) {
        this.name = name;
    }

    /**
     * Add entry to index.
     */
    public void put(long timestamp, long rowId) {
        timeToRowIds.computeIfAbsent(timestamp, k -> new ArrayList<>()).add(rowId);
    }

    /**
     * Get rows valid at timestamp.
     */
    public List<Long> getRowsAt(long timestamp) {
        return timeToRowIds.floorEntry(timestamp) != null ?
                new ArrayList<>(timeToRowIds.floorEntry(timestamp).getValue()) :
                Collections.emptyList();
    }

    /**
     * Get rows in time range.
     */
    public List<Long> getRowsInRange(long startTime, long endTime) {
        List<Long> result = new ArrayList<>();
        timeToRowIds.subMap(startTime, endTime + 1).values()
                .forEach(result::addAll);
        return result;
    }

    /**
     * Get index size.
     */
    public long getSize() {
        return timeToRowIds.values().stream()
                .mapToLong(List::size)
                .sum();
    }
}
