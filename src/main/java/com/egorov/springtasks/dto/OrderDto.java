package com.egorov.springtasks.dto;

import com.egorov.springtasks.entity.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;

    @NotNull(message = "Покупатель обязателен")
    private CustomerDto customer;

    @NotEmpty(message = "Заказ должен содержать хотя бы один продукт")
    private List<ProductDto> products;

    private LocalDateTime orderDate;

    @NotBlank(message = "Адрес доставки обязателен")
    private String shippingAddress;

    private BigDecimal totalPrice;

    private OrderStatus orderStatus;
}
