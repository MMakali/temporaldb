package com.temporaldb.core.management;

import java.util.ArrayList;
import java.util.List;

/**
 * Table schema definition.
 */
public class TableSchema {
    private final String tableName;
    private final List<ColumnDefinition> columns = new ArrayList<>();
    private final long createdAt = System.currentTimeMillis();
    private volatile String description = "";

    public TableSchema(String tableName, List<ColumnDefinition> columns) {
        this.tableName = tableName;
        this.columns.addAll(columns);
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnDefinition> getColumns() {
        return new ArrayList<>(columns);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addColumn(ColumnDefinition column) {
        columns.add(column);
    }

    public ColumnDefinition getColumn(String name) {
        return columns.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
