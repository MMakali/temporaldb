package com.temporaldb.core.memory;

import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OffHeapMemoryManager manages memory allocation using unsafe operations.
 * Provides low-level memory management for data storage.
 */
public class OffHeapMemoryManager implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(OffHeapMemoryManager.class);

    private final long maxMemory;
    private final AtomicLong allocatedMemory;
    private final AtomicLong allocationCount;
    /**
     * -- GETTER --
     *  Check if memory manager is closed.
     *
     * @return True if closed
     */
    @Getter
    private volatile boolean closed = false;

    private static final sun.misc.Unsafe unsafe;

    static {
        try {
            java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (sun.misc.Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Unsafe", e);
        }
    }

    /**
     * Create OffHeapMemoryManager with specified max memory.
     * @param maxMemory Maximum memory in bytes
     */
    public OffHeapMemoryManager(long maxMemory) {
        this.maxMemory = maxMemory;
        this.allocatedMemory = new AtomicLong(0);
        this.allocationCount = new AtomicLong(0);
        logger.info("OffHeapMemoryManager initialized: maxMemory={} bytes", maxMemory);
    }

    /**
     * Allocate off-heap memory.
     * @param size Bytes to allocate
     * @return Address of allocated memory
     * @throws OutOfMemoryError if allocation exceeds limit
     */
    public long allocate(long size) {
        if (closed) {
            throw new IllegalStateException("MemoryManager is closed");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        long current = allocatedMemory.get();
        if (current + size > maxMemory) {
            throw new OutOfMemoryError(
                    String.format("Cannot allocate %d bytes: limit %d exceeded", size, maxMemory)
            );
        }

        try {
            long address = unsafe.allocateMemory(size);
            // Initialize memory to zero
            unsafe.setMemory(address, size, (byte) 0);
            allocatedMemory.addAndGet(size);
            allocationCount.incrementAndGet();
            logger.debug("Allocated {} bytes at address {} (total: {})", size, address, allocatedMemory.get());
            return address;
        } catch (Exception e) {
            logger.error("Allocation failed for {} bytes", size, e);
            throw new MemoryAllocationException("Allocation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Free previously allocated memory.
     * @param address Address returned from allocate()
     * @param size Size of allocation
     */
    public void free(long address, long size) {
        if (closed) {
            logger.warn("Cannot free memory: manager is closed");
            return;
        }

        try {
            unsafe.freeMemory(address);
            allocatedMemory.addAndGet(-size);
            logger.debug("Freed {} bytes from address {}", size, address);
        } catch (Exception e) {
            logger.error("Error freeing memory at address {}", address, e);
        }
    }

    /**
     * Allocate memory for long values.
     * @param count Number of longs to allocate
     * @return Memory address
     */
    public long allocateLongs(long count) {
        return allocate(count * 8);
    }

    /**
     * Allocate memory for double values.
     * @param count Number of doubles to allocate
     * @return Memory address
     */
    public long allocateDoubles(long count) {
        return allocate(count * 8);
    }

    /**
     * Write a long value at offset.
     * @param address Base address
     * @param offset Offset from base address
     * @param value Value to write
     */
    public void putLong(long address, long offset, long value) {
        unsafe.putLong(address + offset, value);
    }

    /**
     * Read a long value at offset.
     * @param address Base address
     * @param offset Offset from base address
     * @return Value at offset
     */
    public long getLong(long address, long offset) {
        return unsafe.getLong(address + offset);
    }

    /**
     * Write a double value at offset.
     * @param address Base address
     * @param offset Offset from base address
     * @param value Value to write
     */
    public void putDouble(long address, long offset, double value) {
        unsafe.putDouble(address + offset, value);
    }

    /**
     * Read a double value at offset.
     * @param address Base address
     * @param offset Offset from base address
     * @return Value at offset
     */
    public double getDouble(long address, long offset) {
        return unsafe.getDouble(address + offset);
    }

    /**
     * Write a byte value at offset.
     * @param address Base address
     * @param offset Offset from base address
     * @param value Value to write
     */
    public void putByte(long address, long offset, byte value) {
        unsafe.putByte(address + offset, value);
    }

    /**
     * Read a byte value at offset.
     * @param address Base address
     * @param offset Offset from base address
     * @return Value at offset
     */
    public byte getByte(long address, long offset) {
        return unsafe.getByte(address + offset);
    }

    /**
     * Copy memory from source to destination.
     * @param src Source address
     * @param dest Destination address
     * @param size Number of bytes to copy
     */
    public void copyMemory(long src, long dest, long size) {
        unsafe.copyMemory(src, dest, size);
    }

    /**
     * Get total allocated memory.
     * @return Bytes allocated
     */
    public long getAllocatedMemory() {
        return allocatedMemory.get();
    }

    /**
     * Get available memory.
     * @return Bytes available
     */
    public long getAvailableMemory() {
        return maxMemory - allocatedMemory.get();
    }

    /**
     * Get memory utilization percentage.
     * @return Percentage (0-100)
     */
    public double getMemoryUtilization() {
        return (allocatedMemory.get() * 100.0) / maxMemory;
    }

    /**
     * Get allocation count.
     * @return Number of allocations
     */
    public long getAllocationCount() {
        return allocationCount.get();
    }

    /**
     * Close the memory manager and free resources.
     */
    @Override
    public void close() {
        if (closed) return;
        closed = true;
        try {
            logger.info("OffHeapMemoryManager closed. Final: {} bytes allocated, {} allocations",
                    allocatedMemory.get(), allocationCount.get());
        } catch (Exception e) {
            logger.error("Error closing MemoryManager", e);
        }
    }
}