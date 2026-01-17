package com.temporaldb.core.memory;

public class MemoryAllocationException extends RuntimeException {
    public MemoryAllocationException(String message) {
        super(message);
    }

    public MemoryAllocationException(String message, Throwable cause) {
        super(message, cause);
    }
}