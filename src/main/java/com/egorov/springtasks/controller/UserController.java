package com.egorov.springtasks.controller;

import com.egorov.springtasks.dto.UserDto;
import com.egorov.springtasks.service.UserService;
import com.egorov.springtasks.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(Views.UserSummary.class)
    public UserDto createUser(@Validated(UserDto.Create.class) @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping
    @JsonView(Views.UserSummary.class)
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @JsonView(Views.UserDetails.class)
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @JsonView(Views.UserSummary.class)
    public UserDto updateUser(@PathVariable Long id, @Validated(UserDto.Update.class) @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
