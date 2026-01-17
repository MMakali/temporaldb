package com.temporaldb.core.storage;

import lombok.Getter;

/**
 * Abstract base class for columnar storage.
 * Supports strongly-typed columns for temporal data.
 */

@Getter
public abstract class Column {
    protected final String name;
    protected final ColumnType type;
    protected final long memoryAddress;
    protected final OffHeapMemoryProvider memoryProvider;
    protected long size;

    /**
     * Create a new Column.
     * @param name Column name
     * @param type Column type
     * @param memoryAddress Memory address for storage
     * @param memoryProvider Provider for memory operations
     */
    public Column(String name, ColumnType type, long memoryAddress, OffHeapMemoryProvider memoryProvider) {
        this.name = name;
        this.type = type;
        this.memoryAddress = memoryAddress;
        this.memoryProvider = memoryProvider;
        this.size = 0;
    }

    /**
     * Append a value to the column.
     * @param value Value to append
     */
    public abstract void append(Object value);

    /**
     * Get value at index.
     * @param index Row index
     * @return Value at index
     */
    public abstract Object get(long index);

    /**
     * Set value at index.
     * @param index Row index
     * @param value New value
     */
    public abstract void set(long index, Object value);


    /**
     * Column data types supported.
     */
    public enum ColumnType {
        LONG("Long integers"),
        DOUBLE("Floating point numbers"),
        STRING("Text strings"),
        BOOLEAN("Boolean values");

        private final String description;

        ColumnType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Interface for memory operations.
     */
    public interface OffHeapMemoryProvider {
        void putLong(long offset, long value);
        long getLong(long offset);
        void putDouble(long offset, double value);
        double getDouble(long offset);
        void putByte(long offset, byte value);
        byte getByte(long offset);
        void putInt(long offset, int value);
        int getInt(long offset);
    }
}

