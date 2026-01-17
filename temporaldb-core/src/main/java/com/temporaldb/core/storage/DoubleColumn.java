package com.temporaldb.core.storage;

/**
 * Column for storing double values.
 * Uses 8 bytes per value for efficient storage.
 */
public class DoubleColumn extends Column {
    private static final int DOUBLE_SIZE = 8;

    /**
     * Create a DoubleColumn.
     * @param name Column name
     * @param memoryAddress Memory address for storage
     * @param memoryProvider Provider for memory operations
     */
    public DoubleColumn(String name, long memoryAddress, Column.OffHeapMemoryProvider memoryProvider) {
        super(name, ColumnType.DOUBLE, memoryAddress, memoryProvider);
    }

    /**
     * Append a double value to the column.
     * @param value Double value to append
     */
    @Override
    public void append(Object value) {
        if (!(value instanceof Double)) {
            throw new IllegalArgumentException("Expected Double, got " + value.getClass().getSimpleName());
        }
        double doubleValue = (Double) value;
        long offset = size * DOUBLE_SIZE;
        memoryProvider.putDouble(offset, doubleValue);
        size++;
    }

    /**
     * Get double value at index.
     * @param index Row index
     * @return Double value
     */
    @Override
    public Object get(long index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " >= size " + size);
        }
        long offset = index * DOUBLE_SIZE;
        return memoryProvider.getDouble(offset);
    }

    /**
     * Set double value at index.
     * @param index Row index
     * @param value New double value
     */
    @Override
    public void set(long index, Object value) {
        if (!(value instanceof Double)) {
            throw new IllegalArgumentException("Expected Double, got " + value.getClass().getSimpleName());
        }
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " >= size " + size);
        }
        long offset = index * DOUBLE_SIZE;
        memoryProvider.putDouble(offset, (Double) value);
    }

    /**
     * Get value as double (for convenience).
     * @param index Row index
     * @return Double value
     */
    public double getDouble(long index) {
        return (Double) get(index);
    }
}
