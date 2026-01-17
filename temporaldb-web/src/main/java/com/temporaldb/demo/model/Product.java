package com.temporaldb.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer quantity;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Create a product from form data.
     */
    public static Product of(ProductForm form) {
        Product product = new Product();
        product.setName(form.getName());
        product.setCategory(form.getCategory());
        product.setPrice(form.getPrice());
        product.setQuantity(form.getQuantity());
        product.setDescription(form.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }
}