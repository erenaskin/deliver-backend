package com.deliver.backend;

import com.deliver.backend.dto.request.ProductRequest;
import com.deliver.backend.dto.response.ProductResponse;
import com.deliver.backend.entity.Product;
import com.deliver.backend.repository.ProductRepository;
import com.deliver.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    private Product testProduct;
    private ProductRequest testProductRequest;
    
    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Pizza")
                .description("Delicious test pizza")
                .category("Pizza")
                .price(new BigDecimal("15.99"))
                .quantity(50)
                .isAvailable(true)
                .isFeatured(false)
                .build();
        
        testProductRequest = ProductRequest.builder()
                .name("Test Pizza")
                .description("Delicious test pizza")
                .category("Pizza")
                .price(new BigDecimal("15.99"))
                .build();
    }
    
    @Test
    void testGetAllProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByIsAvailableTrue()).thenReturn(products);
        
        // When
        List<ProductResponse> result = productService.getAllProducts();
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Pizza", result.get(0).getName());
        verify(productRepository).findByIsAvailableTrue();
    }
    
    @Test
    void testGetProductById() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        // When
        ProductResponse result = productService.getProductById(1L);
        
        // Then
        assertNotNull(result);
        assertEquals("Test Pizza", result.getName());
        assertEquals(new BigDecimal("15.99"), result.getPrice());
        verify(productRepository).findById(1L);
    }
    
    @Test
    void testCreateProduct() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // When
        ProductResponse result = productService.createProduct(testProductRequest);
        
        // Then
        assertNotNull(result);
        assertEquals("Test Pizza", result.getName());
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    void testUpdateProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // When
        ProductResponse result = productService.updateProduct(1L, testProductRequest);
        
        // Then
        assertNotNull(result);
        assertEquals("Test Pizza", result.getName());
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    void testDeleteProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // When
        productService.deleteProduct(1L);
        
        // Then
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }
}
