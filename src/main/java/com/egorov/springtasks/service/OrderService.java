package com.egorov.springtasks.service;


import com.egorov.springtasks.dto.OrderCreateDto;
import com.egorov.springtasks.dto.ProductDto;
import com.egorov.springtasks.entity.Customer;
import com.egorov.springtasks.entity.Order;
import com.egorov.springtasks.entity.OrderStatus;
import com.egorov.springtasks.entity.Product;
import com.egorov.springtasks.exception.ResourceNotFoundException;
import com.egorov.springtasks.repository.CustomerRepository;
import com.egorov.springtasks.repository.OrderRepository;
import com.egorov.springtasks.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ не найден с id: " + id));
    }

    public Order createOrder(OrderCreateDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Покупатель не найден с id: " + dto.getCustomerId()));

        List<Product> products = productRepository.findAllById(dto.getProductIds());

        if (products.size() != dto.getProductIds().size()) {
            throw new IllegalArgumentException("Некоторые продукты не найдены");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setProducts(products);
        order.setShippingAddress(dto.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.NEW);

        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);

        return orderRepository.save(order);
    }
}