package com.temporaldb.core.query;

/**
 * Represents an UPDATE query.
 */
public class UpdateQuery {
    private final String tableName;
    private String setClause;
    private FilterClause whereClause;

    public UpdateQuery(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSetClause() {
        return setClause;
    }

    public void setSetClause(String setClause) {
        this.setClause = setClause;
    }

    public FilterClause getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(FilterClause whereClause) {
        this.whereClause = whereClause;
    }
}
