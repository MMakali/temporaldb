package com.temporaldb.core.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hash index implementation.
 */
public class HashIndex implements DatabaseIndex {
    private static final Logger logger = LoggerFactory.getLogger(HashIndex.class);

    private final String name;
    private final String tableName;
    private final String columnName;
    private final Map<Object, List<Long>> hashTable = new ConcurrentHashMap<>();

    public HashIndex(String name, String tableName, String columnName) {
        this.name = name;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    /**
     * Insert key-value pair into hash index.
     */
    @Override
    public void insert(Object key, long rowId) {
        hashTable.computeIfAbsent(key, k -> new ArrayList<>()).add(rowId);
        logger.debug("Inserted key {} into Hash index {}", key, name);
    }

    /**
     * Search for key in hash index (O(1) average case).
     */
    @Override
    public List<Long> search(Object key) {
        List<Long> result = hashTable.get(key);
        return result != null ? new ArrayList<>(result) : Collections.emptyList();
    }

    /**
     * Range search - not efficient for hash indexes.
     */
    @Override
    public List<Long> rangeSearch(Object startKey, Object endKey) {
        logger.warn("Range search on Hash index {} is inefficient", name);
        return hashTable.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    /**
     * Delete key from hash index.
     */
    @Override
    public void delete(Object key) {
        hashTable.remove(key);
    }

    @Override
    public long getSize() {
        return hashTable.values().stream().mapToLong(List::size).sum();
    }
}
