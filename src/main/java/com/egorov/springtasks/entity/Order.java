package com.egorov.springtasks.entity;


import com.egorov.springtasks.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.UserDetails.class})
    private Long id;

    @JsonView(Views.UserDetails.class)
    @Column(nullable = false)
    private String productName;

    @JsonView(Views.UserDetails.class)
    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @JsonView(Views.UserDetails.class)
    @Column(nullable = false)
    private OrderStatus status;

    @JsonView(Views.UserDetails.class)
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Order(){}

    public Order(String productName, BigDecimal amount) {
        this.productName = productName;
        this.amount = amount;
        this.status = OrderStatus.NEW;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(productName, order.productName) && Objects.equals(amount, order.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, amount);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                ", orderDate=" + orderDate +
                '}';
    }
}
