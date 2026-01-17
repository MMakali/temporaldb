package com.temporaldb.core.query;

import java.util.*;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses SQL-like query strings into executable query objects.
 */
public class QueryParser {
    private static final Logger logger = LoggerFactory.getLogger(QueryParser.class);

    private static final Pattern SELECT_PATTERN = Pattern.compile(
            "SELECT\\s+(.*?)\\s+FROM\\s+(\\w+)(\\s+WHERE\\s+(.*))?",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    private static final Pattern INSERT_PATTERN = Pattern.compile(
            "INSERT\\s+INTO\\s+(\\w+)\\s*\\((.*?)\\)\\s+VALUES\\s*\\((.*?)\\)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern UPDATE_PATTERN = Pattern.compile(
            "UPDATE\\s+(\\w+)\\s+SET\\s+(.*?)(?:\\s+WHERE\\s+(.*))?",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    /**
     * Parse a SELECT query.
     */
    public SelectQuery parseSelect(String sql) {
        var matcher = SELECT_PATTERN.matcher(sql);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid SELECT query: " + sql);
        }

        String columns = matcher.group(1).trim();
        String tableName = matcher.group(2).trim();
        String whereClause = matcher.group(4);

        SelectQuery query = new SelectQuery(tableName);
        query.setColumns(Arrays.asList(columns.split(",")));

        if (whereClause != null && !whereClause.isEmpty()) {
            query.setWhereClause(new FilterClause(whereClause));
        }

        logger.debug("Parsed SELECT query: table={}, columns={}", tableName, columns);
        return query;
    }

    /**
     * Parse an INSERT query.
     */
    public InsertQuery parseInsert(String sql) {
        var matcher = INSERT_PATTERN.matcher(sql);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid INSERT query: " + sql);
        }

        String tableName = matcher.group(1).trim();
        String columns = matcher.group(2);
        String values = matcher.group(3);

        InsertQuery query = new InsertQuery(tableName);
        query.setColumns(Arrays.asList(columns.split(",")));
        query.setValues(Arrays.asList(values.split(",")));

        logger.debug("Parsed INSERT query: table={}, columns={}", tableName, columns);
        return query;
    }

    /**
     * Parse an UPDATE query.
     */
    public UpdateQuery parseUpdate(String sql) {
        var matcher = UPDATE_PATTERN.matcher(sql);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid UPDATE query: " + sql);
        }

        String tableName = matcher.group(1).trim();
        String setClause = matcher.group(2);
        String whereClause = matcher.group(3);

        UpdateQuery query = new UpdateQuery(tableName);
        query.setSetClause(setClause);

        if (whereClause != null && !whereClause.isEmpty()) {
            query.setWhereClause(new FilterClause(whereClause));
        }

        logger.debug("Parsed UPDATE query: table={}", tableName);
        return query;
    }
}

