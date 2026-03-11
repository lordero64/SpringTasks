package com.egorov.springtasks.mapper;

import com.egorov.springtasks.dto.OrderDto;
import com.egorov.springtasks.dto.UserDto;
import com.egorov.springtasks.entity.Order;
import com.egorov.springtasks.entity.User;

import java.util.ArrayList;
import java.util.List;


public class UserMapper {
    private UserMapper() {}

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        return new User(dto.getName(), dto.getEmail());
    }

    public static void updateEntity(User user, UserDto dto) {
        if (dto == null) return;
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto(user.getId(), user.getName(), user.getEmail());

        // Добавляем заказы, если они есть
        if (user.getOrders() != null && !user.getOrders().isEmpty()) {
            List<OrderDto> orderDtos = new ArrayList<>();
            for (Order order : user.getOrders()) {
                orderDtos.add(new OrderDto(
                        order.getId(),
                        order.getProductName(),
                        order.getAmount(),
                        order.getStatus(),
                        order.getOrderDate()
                ));
            }
            dto.setOrders(orderDtos);
        }

        return dto;
    }

    public static List<UserDto> toDtoList(List<User> users) {
        if (users == null) return new ArrayList<>();
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }
}
