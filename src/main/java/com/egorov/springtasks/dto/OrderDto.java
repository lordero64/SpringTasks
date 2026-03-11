package com.egorov.springtasks.dto;

import com.egorov.springtasks.entity.OrderStatus;
import com.egorov.springtasks.views.Views;
import com.fasterxml.jackson.annotation.JsonView;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {

    @JsonView(Views.UserDetails.class)
    private Long id;

    @JsonView(Views.UserDetails.class)
    private String productName;

    @JsonView(Views.UserDetails.class)
    private BigDecimal amount;

    @JsonView(Views.UserDetails.class)
    private OrderStatus status;

    @JsonView(Views.UserDetails.class)
    private LocalDateTime orderDate;

    public OrderDto() {}

    public OrderDto(Long id, String productName, BigDecimal amount,
                    OrderStatus status, LocalDateTime orderDate) {
        this.id = id;
        this.productName = productName;
        this.amount = amount;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
}
