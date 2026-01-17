package com.temporaldb.core.management;

/**
 * Column definition.
 */
public class ColumnDefinition {
    private final String name;
    private final String type;
    private final boolean nullable;
    private final boolean primaryKey;
    private final String defaultValue;

    public ColumnDefinition(String name, String type) {
        this(name, type, true, false, null);
    }

    public ColumnDefinition(String name, String type, boolean nullable,
                            boolean primaryKey, String defaultValue) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.primaryKey = primaryKey;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
