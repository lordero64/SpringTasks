package com.egorov.springtasks.dto;

import com.egorov.springtasks.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.util.List;


public class UserDto {
    @Null(message = "ID должен быть null при создании", groups = Create.class)
    @JsonView(Views.UserSummary.class)
    private Long id;

    @NotBlank(message = "Имя обязательно", groups = Create.class)
    @Size(min = 2, max = 50, message = "Имя от 2 до 50 символов")
    @JsonView(Views.UserSummary.class)
    private String name;

    @NotBlank(message = "Email обязателен", groups = Create.class)
    @Email(message = "Некорректный email")
    @JsonView(Views.UserSummary.class)
    private String email;

    @JsonView(Views.UserDetails.class)
    private List<OrderDto> orders;

    // Группы валидации
    public interface Create {}
    public interface Update {}

    public UserDto() {}

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<OrderDto> getOrders() { return orders; }
    public void setOrders(List<OrderDto> orders) { this.orders = orders; }
}
