package com.temporaldb.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Statistics for the inventory system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStats {
    private Integer totalProducts;
    private Integer totalQuantity;
    private Double totalValue;
    private Double averagePrice;
    private Integer lastHourChanges;
    private Integer todayChanges;
}