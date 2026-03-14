package com.egorov.springtasks.service;

import com.egorov.springtasks.dto.ProductDto;
import com.egorov.springtasks.entity.Product;
import com.egorov.springtasks.exception.ResourceNotFoundException;
import com.egorov.springtasks.mapper.ProductMapper;
import com.egorov.springtasks.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт не найден с id: " + id));
    }

    public Product createProduct(Product product) {
        // Дополнительная бизнес-валидация при необходимости
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDto productDto) {
        Product product = getProductById(id);
        productMapper.updateEntity(productDto, product);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Продукт не найден с id: " + id);
        }
        productRepository.deleteById(id);
    }
}
