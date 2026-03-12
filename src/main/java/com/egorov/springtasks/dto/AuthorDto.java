package com.egorov.springtasks.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthorDto {
    private Long id;

    @NotBlank(message = "Имя автора обязательно")
    private String name;

    public AuthorDto() {
    }

    public AuthorDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
