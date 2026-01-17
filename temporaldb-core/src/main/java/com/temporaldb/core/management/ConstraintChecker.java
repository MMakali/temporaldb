package com.temporaldb.core.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Checks constraint violations.
 */
public class ConstraintChecker {
    private static final Logger logger = LoggerFactory.getLogger(ConstraintChecker.class);

    private final Map<String, Set<Object>> primaryKeyValues = new ConcurrentHashMap<>();

    /**
     * Check primary key constraint.
     */
    public boolean checkPrimaryKey(TableSchema schema, Map<String, Object> row) {
        for (ColumnDefinition column : schema.getColumns()) {
            if (column.isPrimaryKey()) {
                Object value = row.get(column.getName());

                Set<Object> keys = primaryKeyValues.computeIfAbsent(
                        schema.getTableName(), k -> ConcurrentHashMap.newKeySet()
                );

                if (keys.contains(value)) {
                    logger.warn("Primary key constraint violation for column: {}", column.getName());
                    return false;
                }

                keys.add(value);
            }
        }

        return true;
    }
}
