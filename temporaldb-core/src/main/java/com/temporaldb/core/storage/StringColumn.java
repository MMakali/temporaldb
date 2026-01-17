package com.temporaldb.core.storage;

/**
 * Column for storing String values using dictionary encoding.
 * Reduces storage space by mapping strings to integer indexes.
 */
public class StringColumn extends Column {
    private final java.util.Map<String, Integer> dictionary = new java.util.HashMap<>();
    private final java.util.List<String> reverseDictionary = new java.util.ArrayList<>();
    private static final int INT_SIZE = 4;

    /**
     * Create a StringColumn.
     * @param name Column name
     * @param memoryAddress Memory address for storage
     * @param memoryProvider Provider for memory operations
     */
    public StringColumn(String name, long memoryAddress, Column.OffHeapMemoryProvider memoryProvider) {
        super(name, ColumnType.STRING, memoryAddress, memoryProvider);
    }

    /**
     * Append a string value to the column.
     * @param value String value to append
     */
    @Override
    public void append(Object value) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Expected String, got " + value.getClass().getSimpleName());
        }

        String stringValue = (String) value;
        int dictIndex = dictionary.computeIfAbsent(stringValue, k -> {
            int idx = reverseDictionary.size();
            reverseDictionary.add(k);
            return idx;
        });

        long offset = size * INT_SIZE;
        memoryProvider.putInt(offset, dictIndex);
        size++;
    }

    /**
     * Get string value at index.
     * @param index Row index
     * @return String value
     */
    @Override
    public Object get(long index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " >= size " + size);
        }
        long offset = index * INT_SIZE;
        int dictIndex = memoryProvider.getInt(offset);
        if (dictIndex < 0 || dictIndex >= reverseDictionary.size()) {
            throw new IndexOutOfBoundsException("Invalid dictionary index: " + dictIndex);
        }
        return reverseDictionary.get(dictIndex);
    }

    /**
     * Set string value at index.
     * @param index Row index
     * @param value New string value
     */
    @Override
    public void set(long index, Object value) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Expected String, got " + value.getClass().getSimpleName());
        }
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " >= size " + size);
        }

        String stringValue = (String) value;
        int dictIndex = dictionary.computeIfAbsent(stringValue, k -> {
            int idx = reverseDictionary.size();
            reverseDictionary.add(k);
            return idx;
        });

        long offset = index * INT_SIZE;
        memoryProvider.putInt(offset, dictIndex);
    }

    /**
     * Get value as string (for convenience).
     * @param index Row index
     * @return String value
     */
    public String getString(long index) {
        return (String) get(index);
    }

    /**
     * Get dictionary size.
     * @return Number of unique strings
     */
    public int getDictionarySize() {
        return reverseDictionary.size();
    }

    /**
     * Get compression ratio.
     * @return Ratio of storage used
     */
    public double getCompressionRatio() {
        if (size == 0) return 1.0;
        return (double) reverseDictionary.size() / size;
    }
}