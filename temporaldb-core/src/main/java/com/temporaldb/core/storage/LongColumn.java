package com.temporaldb.core.storage;

/**
 * Column for storing long values.
 * Uses 8 bytes per value for efficient storage.
 */
public class LongColumn extends Column {
    private static final int LONG_SIZE = 8;

    /**
     * Create a LongColumn.
     * @param name Column name
     * @param memoryAddress Memory address for storage
     * @param memoryProvider Provider for memory operations
     */
    public LongColumn(String name, long memoryAddress, Column.OffHeapMemoryProvider memoryProvider) {
        super(name, ColumnType.LONG, memoryAddress, memoryProvider);
    }

    /**
     * Append a long value to the column.
     * @param value Long value to append
     */
    @Override
    public void append(Object value) {
        if (!(value instanceof Long)) {
            throw new IllegalArgumentException("Expected Long, got " + value.getClass().getSimpleName());
        }
        long longValue = (Long) value;
        long offset = size * LONG_SIZE;
        memoryProvider.putLong(offset, longValue);
        size++;
    }

    /**
     * Get long value at index.
     * @param index Row index
     * @return Long value
     */
    @Override
    public Object get(long index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " >= size " + size);
        }
        long offset = index * LONG_SIZE;
        return memoryProvider.getLong(offset);
    }

    /**
     * Set long value at index.
     * @param index Row index
     * @param value New long value
     */
    @Override
    public void set(long index, Object value) {
        if (!(value instanceof Long)) {
            throw new IllegalArgumentException("Expected Long, got " + value.getClass().getSimpleName());
        }
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " >= size " + size);
        }
        long offset = index * LONG_SIZE;
        memoryProvider.putLong(offset, (Long) value);
    }

    /**
     * Get value as long (for convenience).
     * @param index Row index
     * @return Long value
     */
    public long getLong(long index) {
        return (Long) get(index);
    }
}