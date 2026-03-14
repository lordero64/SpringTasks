package com.egorov.springtasks.controller;

import com.egorov.springtasks.dto.ProductDto;
import com.egorov.springtasks.entity.Product;
import com.egorov.springtasks.mapper.ProductMapper;
import com.egorov.springtasks.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService productService;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductController productController;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    private Product product;
    private ProductDto productDto;
    private String productJson;
    private List<Product> productList;
    private List<ProductDto> productDtoList;

    @BeforeEach
    void setUp() throws JsonProcessingException {

        // Создаем тестовый продукт
        product = new Product();
        product.setProductId(1L);
        product.setName("Ноутбук");
        product.setDescription("Игровой ноутбук");
        product.setPrice(new BigDecimal("50000"));
        product.setQuantityInStock(10);

        // Создаем тестовый DTO
        productDto = new ProductDto();
        productDto.setProductId(1L);
        productDto.setName("Ноутбук");
        productDto.setDescription("Игровой ноутбук");
        productDto.setPrice(new BigDecimal("50000"));
        productDto.setQuantityInStock(10);

        // Создаем списки
        productList = List.of(product);
        productDtoList = List.of(productDto);

        // JSON для запросов
        productJson = objectMapper.writeValueAsString(productDto);
    }

    @Test
    void getAllProductsShouldReturnList() throws IOException {
        when(productService.getAllProducts()).thenReturn(productList);
        when(productMapper.toDtoList(anyList())).thenReturn(productDtoList);

        ResponseEntity<String> response = productController.getAllProducts();
        String json = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json).isNotNull();
        assertThat(json).contains("Ноутбук");
        assertThat(json).contains("50000");
        assertThat(json).contains("1");

        verify(productService, times(1)).getAllProducts();
        verify(productMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void getProductByIdShouldReturnProduct() throws IOException {
        when(productService.getProductById(1L)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ResponseEntity<String> response = productController.getProductById(1L);
        String json = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json).isNotNull();
        assertThat(json).contains("Ноутбук");
        assertThat(json).contains("1");

        verify(productService, times(1)).getProductById(1L);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void createProductShouldReturnCreatedProduct() throws IOException {
        when(productMapper.toEntity(any(ProductDto.class))).thenReturn(product);
        when(productService.createProduct(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ResponseEntity<String> response = productController.createProduct(productJson);
        String json = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(json).isNotNull();
        assertThat(json).contains("Ноутбук");
        assertThat(json).contains("1");

        verify(productMapper, times(1)).toEntity(any(ProductDto.class));
        verify(productService, times(1)).createProduct(any(Product.class));
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void updateProductShouldReturnUpdatedProduct() throws IOException {
        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ResponseEntity<String> response = productController.updateProduct(1L, productJson);
        String json = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json).isNotNull();
        assertThat(json).contains("Ноутбук");

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductDto.class));
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void deleteProductShouldReturnNoContent() throws IOException {
        ResponseEntity<String> response = productController.deleteProduct(1L);
        String json = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(json).isNotNull();
        assertThat(json).contains("успешно удален");

        verify(productService, times(1)).deleteProduct(1L);
    }
}