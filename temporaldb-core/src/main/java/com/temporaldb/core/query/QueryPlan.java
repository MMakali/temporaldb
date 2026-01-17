package com.temporaldb.core.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query execution plan.
 */
public class QueryPlan {
    private final SelectQuery query;
    private final List<QueryOperation> operations = new ArrayList<>();
    private int estimatedCost = 0;

    public QueryPlan(SelectQuery query) {
        this.query = query;
    }

    public SelectQuery getQuery() {
        return query;
    }

    public void addOperation(QueryOperation operation) {
        operations.add(operation);
        estimatedCost += operation.getEstimatedCost();
    }

    public List<QueryOperation> getOperations() {
        return operations;
    }

    public int getEstimatedCost() {
        return estimatedCost;
    }

    /**
     * Represents a single operation in the plan.
     */
    public interface QueryOperation {
        String getType();

        int getEstimatedCost();
    }
}
