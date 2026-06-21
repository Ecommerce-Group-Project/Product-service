package com.ecommerce.productservice.service;

import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setProductId(1L);
        sampleProduct.setName("Laptop");
        sampleProduct.setUnitPrice(999.99);
        sampleProduct.setDescription("High performance laptop");
        sampleProduct.setCategory("Electronics");
        sampleProduct.setStock(50);
    }

    // ── createProduct ──────────────────────────────────────────────────────

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        Product result = productService.createProduct(sampleProduct);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(999.99, result.getUnitPrice());
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    void createProduct_ShouldCallRepositorySave() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        productService.createProduct(sampleProduct);

        verify(productRepository).save(sampleProduct);
    }

    // ── getProductById ─────────────────────────────────────────────────────

    @Test
    void getProductById_WhenExists_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals("Laptop", result.getName());
    }

    @Test
    void getProductById_WhenNotExists_ShouldThrowProductNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void getProductById_WhenNotExists_ShouldHaveCorrectMessage() {
        when(productRepository.findById(5L)).thenReturn(Optional.empty());

        ProductNotFoundException ex = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(5L)
        );
        assertTrue(ex.getMessage().contains("5"));
    }

    // ── deleteProduct ──────────────────────────────────────────────────────

    @Test
    void deleteProduct_WhenExists_ShouldDeleteSuccessfully() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_WhenNotExists_ShouldThrowProductNotFoundException() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(99L));
        verify(productRepository, never()).deleteById(any());
    }
}
