package com.temporaldb.core.query;

import java.util.List;

/**
 * Represents a SELECT query.
 */
public class SelectQuery {
    private final String tableName;
    private List<String> columns;
    private FilterClause whereClause;
    private String orderBy;
    private int limit = -1;
    private int offset = 0;
    private boolean temporal = false;
    private long temporalTimestamp;

    public SelectQuery(String tableName) {
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

    public FilterClause getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(FilterClause whereClause) {
        this.whereClause = whereClause;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isTemporal() {
        return temporal;
    }

    public void setTemporal(boolean temporal, long timestamp) {
        this.temporal = temporal;
        this.temporalTimestamp = timestamp;
    }

    public long getTemporalTimestamp() {
        return temporalTimestamp;
    }
}
