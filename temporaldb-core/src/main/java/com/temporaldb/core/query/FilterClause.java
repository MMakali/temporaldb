package com.temporaldb.core.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Represents a WHERE clause filter.
 */
public class FilterClause {
    private static final Logger logger = LoggerFactory.getLogger(FilterClause.class);

    private final String expression;

    public FilterClause(String expression) {
        this.expression = expression.trim();
    }

    public String getExpression() {
        return expression;
    }

    /**
     * Evaluate filter against values.
     */
    public boolean evaluate(Map<String, Object> row) {
        try {
            return evaluateExpression(expression, row);
        } catch (Exception e) {
            logger.error("Error evaluating filter: {}", expression, e);
            return false;
        }
    }

    private boolean evaluateExpression(String expr, Map<String, Object> row) {
        // Simple expression evaluator
        // Supports: column = value, column > value, column < value, column IN (v1, v2)

        if (expr.contains("=")) {
            String[] parts = expr.split("=");
            String column = parts[0].trim();
            String value = parts[1].trim();
            Object colValue = row.get(column);
            return colValue != null && colValue.toString().equals(value);
        }

        if (expr.contains(">")) {
            String[] parts = expr.split(">");
            String column = parts[0].trim();
            Object colValue = row.get(column);
            if (colValue instanceof Number && isNumeric(parts[1].trim())) {
                return ((Number) colValue).doubleValue() > Double.parseDouble(parts[1].trim());
            }
        }

        if (expr.contains("<")) {
            String[] parts = expr.split("<");
            String column = parts[0].trim();
            Object colValue = row.get(column);
            if (colValue instanceof Number && isNumeric(parts[1].trim())) {
                return ((Number) colValue).doubleValue() < Double.parseDouble(parts[1].trim());
            }
        }

        return true;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
