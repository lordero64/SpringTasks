package com.egorov.springtasks.controller;

import com.egorov.springtasks.dto.CustomerDto;
import com.egorov.springtasks.dto.OrderCreateDto;
import com.egorov.springtasks.dto.OrderDto;
import com.egorov.springtasks.entity.Order;
import com.egorov.springtasks.entity.OrderStatus;
import com.egorov.springtasks.mapper.OrderMapper;
import com.egorov.springtasks.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    private OrderService orderService;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderController orderController;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private Order order;
    private OrderDto orderDto;
    private OrderCreateDto orderCreateDto;
    private String orderCreateJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        // Создаем тестового покупателя
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(1L);
        customerDto.setFirstName("Егор");
        customerDto.setLastName("Егоров");
        customerDto.setEmail("ivan@mail.ru");

        // Создаем тестовый заказ
        order = new Order();
        order.setOrderId(1L);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.NEW);
        order.setTotalPrice(new BigDecimal("51000"));
        order.setShippingAddress("Москва");

        // Создаем тестовый DTO заказа
        orderDto = new OrderDto();
        orderDto.setOrderId(1L);
        orderDto.setCustomer(customerDto);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setOrderStatus(OrderStatus.NEW);
        orderDto.setTotalPrice(new BigDecimal("51000"));
        orderDto.setShippingAddress("Москва");

        // Создаем DTO для создания заказа
        orderCreateDto = new OrderCreateDto();
        orderCreateDto.setCustomerId(1L);
        orderCreateDto.setProductIds(List.of(1L, 2L));
        orderCreateDto.setShippingAddress("Москва");

        // JSON для запроса создания
        orderCreateJson = objectMapper.writeValueAsString(orderCreateDto);
    }

    @Test
    void createOrderShouldReturnCreatedOrder() throws IOException {
        when(orderService.createOrder(any(OrderCreateDto.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        ResponseEntity<String> response = orderController.createOrder(orderCreateJson);
        String json = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(json).isNotNull();
        assertThat(json).contains("1");
        assertThat(json).contains("NEW");
        assertThat(json).contains("51000");
        assertThat(json).contains("Москва");

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    void getOrderByIdShouldReturnOrder() throws IOException {
        when(orderService.getOrderById(1L)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        ResponseEntity<String> response = orderController.getOrderById(1L);
        String json = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json).isNotNull();
        assertThat(json).contains("1");
        assertThat(json).contains("NEW");
        assertThat(json).contains("51000");

        verify(orderService, times(1)).getOrderById(1L);
        verify(orderMapper, times(1)).toDto(order);
    }
}