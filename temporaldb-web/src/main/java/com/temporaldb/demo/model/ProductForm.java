package com.temporaldb.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductForm {
    private String name;
    private String category;
    private Double price;
    private Integer quantity;
    private String description;
}