package com.temporaldb.demo.service;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.demo.model.Product;
import com.temporaldb.demo.model.ProductForm;
import com.temporaldb.demo.model.ProductHistory;
import com.temporaldb.demo.model.InventoryStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service layer for managing products using TemporalDB.
 */
@Slf4j
@Service
public class ProductService {

    private final TemporalDBServer database;
    private final Map<Long, Product> products = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<Long, List<ProductHistory>> history = Collections.synchronizedMap(new HashMap<>());
    private Long nextId = 1L;

    public ProductService(TemporalDBServer database) {
        this.database = database;
        initializeDatabase();
    }

    /**
     * Initialize database with sample data.
     */
    private void initializeDatabase() {
        try {
            // Create sample products
            addProduct(new ProductForm(
                    "Laptop",
                    "Electronics",
                    999.99,
                    5,
                    "High performance laptop"
            ));

            addProduct(new ProductForm(
                    "Monitor",
                    "Electronics",
                    299.99,
                    12,
                    "4K Monitor"
            ));

            addProduct(new ProductForm(
                    "Keyboard",
                    "Electronics",
                    79.99,
                    25,
                    "Mechanical keyboard"
            ));

            addProduct(new ProductForm(
                    "Mouse",
                    "Electronics",
                    49.99,
                    30,
                    "Wireless mouse"
            ));

            log.info("Database initialized with {} products", products.size());
        } catch (Exception e) {
            log.error("Failed to initialize database", e);
        }
    }

    /**
     * Add a new product.
     */
    public Product addProduct(ProductForm form) {
        Product product = Product.of(form);
        product.setId(nextId++);
        product.setCreatedAt(LocalDateTime.now());

        products.put(product.getId(), product);
        recordHistory(product, "CREATED");

        log.info("Product created: {} ({})", product.getName(), product.getId());
        return product;
    }

    /**
     * Get all products.
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    /**
     * Get product by ID.
     */
    public Product getProduct(Long id) {
        return products.get(id);
    }

    /**
     * Update a product.
     */
    public Product updateProduct(Long id, ProductForm form) {
        Product existing = products.get(id);
        if (existing == null) {
            throw new IllegalArgumentException("Product not found: " + id);
        }

        Product updated = Product.of(form);
        updated.setId(id);
        updated.setCreatedAt(existing.getCreatedAt());
        updated.setUpdatedAt(LocalDateTime.now());

        // Track changes
        StringBuilder changes = new StringBuilder("UPDATED:");
        if (!existing.getPrice().equals(updated.getPrice())) {
            changes.append(" price ").append(existing.getPrice()).append("→").append(updated.getPrice());
        }
        if (!existing.getQuantity().equals(updated.getQuantity())) {
            changes.append(" qty ").append(existing.getQuantity()).append("→").append(updated.getQuantity());
        }
        if (!existing.getName().equals(updated.getName())) {
            changes.append(" name ").append(existing.getName()).append("→").append(updated.getName());
        }

        products.put(id, updated);
        recordHistory(updated, changes.toString());

        log.info("Product updated: {} {}", id, changes);
        return updated;
    }

    /**
     * Delete a product.
     */
    public void deleteProduct(Long id) {
        Product product = products.remove(id);
        if (product != null) {
            recordHistory(product, "DELETED");
            log.info("Product deleted: {} ({})", product.getName(), id);
        }
    }

    /**
     * Record product history for temporal tracking.
     */
    private void recordHistory(Product product, String change) {
        ProductHistory entry = new ProductHistory(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                LocalDateTime.now(),
                change
        );

        history.computeIfAbsent(product.getId(), k -> new ArrayList<>()).add(entry);
    }

    /**
     * Get product history (time-travel query).
     */
    public List<ProductHistory> getProductHistory(Long productId) {
        return history.getOrDefault(productId, new ArrayList<>());
    }

    /**
     * Get product as of a specific timestamp.
     */
    public Product getProductAsOf(Long productId, LocalDateTime timestamp) {
        List<ProductHistory> productHistory = history.get(productId);
        if (productHistory == null || productHistory.isEmpty()) {
            return null;
        }

        // Find the most recent version at or before the timestamp
        return productHistory.stream()
                .filter(h -> !h.getTimestamp().isAfter(timestamp))
                .max(Comparator.comparing(ProductHistory::getTimestamp))
                .map(h -> new Product(
                        h.getId(),
                        h.getName(),
                        null,
                        h.getPrice(),
                        h.getQuantity(),
                        null,
                        null,
                        h.getTimestamp()
                ))
                .orElse(null);
    }

    /**
     * Get inventory statistics.
     */
    public InventoryStats getStats() {
        InventoryStats stats = new InventoryStats();

        stats.setTotalProducts(products.size());

        int totalQty = products.values().stream()
                .mapToInt(Product::getQuantity)
                .sum();
        stats.setTotalQuantity(totalQty);

        double totalVal = products.values().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
        stats.setTotalValue(totalVal);

        double avgPrice = products.values().stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0.0);
        stats.setAveragePrice(avgPrice);

        // Count recent changes
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long recentChanges = history.values().stream()
                .flatMap(List::stream)
                .filter(h -> h.getTimestamp().isAfter(oneHourAgo))
                .count();
        stats.setLastHourChanges((int) recentChanges);

        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long todayChanges = history.values().stream()
                .flatMap(List::stream)
                .filter(h -> h.getTimestamp().isAfter(today))
                .count();
        stats.setTodayChanges((int) todayChanges);

        return stats;
    }

    /**
     * Get audit trail (all changes).
     */
    public List<ProductHistory> getAuditTrail() {
        return history.values().stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(ProductHistory::getTimestamp).reversed())
                .limit(100)
                .toList();
    }
}