package com.egorov.springtasks.mapper;

import com.egorov.springtasks.dto.OrderCreateDto;
import com.egorov.springtasks.dto.OrderDto;
import com.egorov.springtasks.entity.Order;
import com.egorov.springtasks.entity.OrderStatus;
import org.mapstruct.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, ProductMapper.class})
public interface OrderMapper {

    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    OrderDto toDto(Order order);

    List<OrderDto> toDtoList(List<Order> orders);

    @AfterMapping
    default void setOrderDefaults(@MappingTarget Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.NEW);
    }

    @AfterMapping
    default void calculateTotalPrice(@MappingTarget Order order) {
        BigDecimal total = order.getProducts().stream()
                .map(product -> product.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);
    }
}
