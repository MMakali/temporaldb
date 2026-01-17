package com.temporaldb.core.management;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages database schemas and table definitions.
 */
public class SchemaManager {
    private static final Logger logger = LoggerFactory.getLogger(SchemaManager.class);

    private final Map<String, TableSchema> schemas = new ConcurrentHashMap<>();

    /**
     * Create table schema.
     */
    public TableSchema createTable(String tableName, List<ColumnDefinition> columns) {
        TableSchema schema = new TableSchema(tableName, columns);
        schemas.put(tableName, schema);
        logger.info("Created table schema: {} with {} columns", tableName, columns.size());
        return schema;
    }

    /**
     * Get table schema.
     */
    public TableSchema getTableSchema(String tableName) {
        return schemas.get(tableName);
    }

    /**
     * List all tables.
     */
    public Collection<TableSchema> getAllTables() {
        return schemas.values();
    }

    /**
     * Drop table.
     */
    public boolean dropTable(String tableName) {
        boolean removed = schemas.remove(tableName) != null;
        if (removed) {
            logger.info("Dropped table: {}", tableName);
        }
        return removed;
    }

    /**
     * Add column to table.
     */
    public void addColumn(String tableName, ColumnDefinition column) {
        TableSchema schema = schemas.get(tableName);
        if (schema != null) {
            schema.addColumn(column);
            logger.info("Added column {} to table {}", column.getName(), tableName);
        }
    }
}

