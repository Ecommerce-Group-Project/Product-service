package com.ecommerce.productservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product")
@Data
@Schema(description = "Product entity")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique product ID", example = "1")
    private Long productId;

    @Schema(description = "Product name", example = "Laptop")
    private String name;

    @Schema(description = "Price per unit", example = "999.99")
    private Double unitPrice;

    @Schema(description = "Product description", example = "High performance laptop")
    private String description;

    @Schema(description = "Product category", example = "Electronics")
    private String category;

    @Schema(description = "Available stock", example = "50")
    private Integer stock;
}
