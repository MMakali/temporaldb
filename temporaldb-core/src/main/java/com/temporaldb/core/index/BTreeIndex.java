package com.temporaldb.core.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * B-Tree index implementation.
 */
public class BTreeIndex implements DatabaseIndex {
    private static final Logger logger = LoggerFactory.getLogger(BTreeIndex.class);

    private final String name;
    private final String tableName;
    private final String columnName;
    private final NavigableMap<Object, List<Long>> tree = new TreeMap<>();

    public BTreeIndex(String name, String tableName, String columnName) {
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
     * Insert key-value pair into B-Tree.
     */
    @Override
    public void insert(Object key, long rowId) {
        tree.computeIfAbsent(key, k -> new ArrayList<>()).add(rowId);
        logger.debug("Inserted key {} into B-Tree index {}", key, name);
    }

    /**
     * Search for key in B-Tree.
     */
    @Override
    public List<Long> search(Object key) {
        List<Long> result = tree.get(key);
        return result != null ? new ArrayList<>(result) : Collections.emptyList();
    }

    /**
     * Range search in B-Tree.
     */
    @Override
    public List<Long> rangeSearch(Object startKey, Object endKey) {
        List<Long> result = new ArrayList<>();
        tree.subMap(startKey, true, endKey, true).values()
                .forEach(result::addAll);
        return result;
    }

    /**
     * Delete key from B-Tree.
     */
    @Override
    public void delete(Object key) {
        tree.remove(key);
    }

    @Override
    public long getSize() {
        return tree.values().stream().mapToLong(List::size).sum();
    }
}
