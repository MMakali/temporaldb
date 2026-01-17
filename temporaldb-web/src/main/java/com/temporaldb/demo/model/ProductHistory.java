package com.temporaldb.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO for displaying product history.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductHistory {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private LocalDateTime timestamp;
    private String change;
}