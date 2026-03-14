package com.egorov.springtasks.controller;

import com.egorov.springtasks.dto.OrderCreateDto;
import com.egorov.springtasks.dto.OrderDto;
import com.egorov.springtasks.entity.Order;
import com.egorov.springtasks.mapper.OrderMapper;
import com.egorov.springtasks.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody String jsonBody) throws IOException {
        OrderCreateDto createDto = objectMapper.readValue(jsonBody, OrderCreateDto.class);
        Order order = orderService.createOrder(createDto);
        OrderDto result = orderMapper.toDto(order);

        String json = objectMapper.writeValueAsString(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable Long id) throws IOException {
        Order order = orderService.getOrderById(id);
        OrderDto dto = orderMapper.toDto(order);

        String json = objectMapper.writeValueAsString(dto);
        return ResponseEntity.ok(json);
    }
}
