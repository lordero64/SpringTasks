package com.egorov.springtasks.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {
    @NotNull(message = "ID покупателя обязателен")
    private Long customerId;

    @NotEmpty(message = "Заказ должен содержать хотя бы один продукт")
    private List<Long> productIds;

    @NotBlank(message = "Адрес доставки обязателен")
    private String shippingAddress;

    @NotNull(message = "Общая сумма обязательна")
    @Positive(message = "Сумма должна быть положительной")
    private BigDecimal totalPrice;
}

