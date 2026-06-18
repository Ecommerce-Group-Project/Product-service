package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {

        this.productService = productService;
    }

    @Operation(summary = "Create new Product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product Created Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }
}
