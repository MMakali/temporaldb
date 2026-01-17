package com.temporaldb.core.query;

import java.util.List;

/**
 * Represents an INSERT query.
 */
public class InsertQuery {
    private final String tableName;
    private List<String> columns;
    private List<String> values;

    public InsertQuery(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
