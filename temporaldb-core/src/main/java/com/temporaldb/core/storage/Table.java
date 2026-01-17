package com.temporaldb.core.storage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In-memory table with columnar storage.
 */
public class Table {
    private static final Logger logger = LoggerFactory.getLogger(Table.class);

    @Getter
    private final String name;

    private final Map<String, Column> columns = new ConcurrentHashMap<>();
    private final List<String> columnOrder = new ArrayList<>();

    @Getter
    private long rowCount = 0;

    public Table(String name) {
        this.name = name;
        logger.info("Created table: {}", name);
    }

    public void addColumn(Column column) {
        columns.put(column.getName(), column);
        columnOrder.add(column.getName());
        logger.debug("Added column {} to table {}", column.getName(), name);
    }

    public void insertRow(Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            Column column = columns.get(entry.getKey());
            if (column == null) {
                throw new IllegalArgumentException("Column not found: " + entry.getKey());
            }
            column.append(entry.getValue());
        }
        rowCount++;
    }

    public Map<String, Object> getRow(long rowIndex) {
        Map<String, Object> row = new LinkedHashMap<>();
        for (String colName : columnOrder) {
            Column column = columns.get(colName);
            row.put(colName, column.get(rowIndex));
        }
        return row;
    }

    public Collection<Column> getColumns() { return columns.values(); }
}