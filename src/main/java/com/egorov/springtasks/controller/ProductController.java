package com.egorov.springtasks.controller;

import com.egorov.springtasks.dto.ProductDto;
import com.egorov.springtasks.entity.Product;
import com.egorov.springtasks.mapper.ProductMapper;
import com.egorov.springtasks.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<String> getAllProducts() throws IOException {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> dtos = productMapper.toDtoList(products);

        String json = objectMapper.writeValueAsString(dtos);
        return ResponseEntity.ok(json);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) throws IOException {
        Product product = productService.getProductById(id);
        ProductDto dto = productMapper.toDto(product);

        String json = objectMapper.writeValueAsString(dto);
        return ResponseEntity.ok(json);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody String jsonBody) throws IOException {
        ProductDto dto = objectMapper.readValue(jsonBody, ProductDto.class);
        Product product = productMapper.toEntity(dto);
        Product created = productService.createProduct(product);
        ProductDto result = productMapper.toDto(created);

        String json = objectMapper.writeValueAsString(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestBody String jsonBody) throws IOException {

        ProductDto dto = objectMapper.readValue(jsonBody, ProductDto.class);
        Product updated = productService.updateProduct(id, dto);
        ProductDto result = productMapper.toDto(updated);

        String json = objectMapper.writeValueAsString(result);
        return ResponseEntity.ok(json);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);

        String json = objectMapper.writeValueAsString("Продукт успешно удален");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(json);
    }
}
