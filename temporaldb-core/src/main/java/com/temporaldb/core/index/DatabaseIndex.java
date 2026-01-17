package com.temporaldb.core.index;

import java.util.List;

/**
 * Base interface for database indexes.
 */
public interface DatabaseIndex {
    String getName();

    String getTableName();

    String getColumnName();

    void insert(Object key, long rowId);

    List<Long> search(Object key);

    List<Long> rangeSearch(Object startKey, Object endKey);

    void delete(Object key);

    long getSize();
}
