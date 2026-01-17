package com.temporaldb.core.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Validates data against schema constraints.
 */
public class DataValidator {
    private static final Logger logger = LoggerFactory.getLogger(DataValidator.class);

    /**
     * Validate row data against schema.
     */
    public boolean validate(TableSchema schema, Map<String, Object> row) {
        for (ColumnDefinition column : schema.getColumns()) {
            Object value = row.get(column.getName());

            // Check not null constraint
            if (value == null && !column.isNullable()) {
                logger.warn("Null value for non-nullable column: {}", column.getName());
                return false;
            }

            // Check type match
            if (value != null && !validateType(value, column.getType())) {
                logger.warn("Type mismatch for column {}: expected {}, got {}",
                        column.getName(), column.getType(), value.getClass().getSimpleName());
                return false;
            }
        }

        return true;
    }

    private boolean validateType(Object value, String columnType) {
        return switch (columnType.toUpperCase()) {
            case "LONG" -> value instanceof Long;
            case "DOUBLE" -> value instanceof Double;
            case "STRING" -> value instanceof String;
            case "BOOLEAN" -> value instanceof Boolean;
            case "INT" -> value instanceof Integer;
            default -> true;
        };
    }
}
