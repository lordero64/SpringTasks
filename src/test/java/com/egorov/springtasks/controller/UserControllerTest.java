package com.egorov.springtasks.controller;

import com.egorov.springtasks.dto.OrderDto;
import com.egorov.springtasks.dto.UserDto;
import com.egorov.springtasks.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private ObjectMapper objectMapper = new ObjectMapper();
    private UserDto user;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new UserDto(1L, "Иван", "ivan@mail.ru");
        OrderDto order = new OrderDto();
        order.setId(1L);
        order.setProductName("Телефон");
        order.setAmount(new BigDecimal("50000"));
        user.setOrders(List.of(order));
    }

    @Test
    void createUser() throws Exception {

        when(userService.createUser(any())).thenReturn(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("orders").doesNotExist());

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void getAllUsers() throws Exception {

        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Иван"))
                .andExpect(jsonPath("$[0].email").value("ivan@mail.ru"))
                .andExpect(jsonPath("$[0].orders").doesNotExist()); // JsonView скрывает поле

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById() throws Exception {

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Иван"))
                .andExpect(jsonPath("$.email").value("ivan@mail.ru"))
                .andExpect(jsonPath("$.orders").exists())           // JsonView показывает поле
                .andExpect(jsonPath("$.orders[0].productName").value("Телефон"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void updateUser() throws Exception {

        UserDto updateData = new UserDto();
        updateData.setName("Петр");
        updateData.setEmail("petr@mail.ru");

        UserDto updatedUser = new UserDto(1L, "Петр", "petr@mail.ru");

        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(updatedUser);

        // Действие и проверка
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Петр"))
                .andExpect(jsonPath("$.email").value("petr@mail.ru"))
                .andExpect(jsonPath("$.orders").doesNotExist()); // JsonView скрывает поле

        verify(userService, times(1)).updateUser(eq(1L), any(UserDto.class));
    }

    @Test
    void deleteUser() throws Exception {

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(userService, times(1)).deleteUser(1L);
    }
}