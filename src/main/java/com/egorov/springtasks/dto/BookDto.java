package com.egorov.springtasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Year;

public class BookDto {
    private Long id;

    @NotBlank(message = "Название книги обязательно")
    private String title;

    @NotBlank(message = "ISBN обязателен")
    private String isbn;

    @NotNull(message = "Год издания обязателен")
    @Positive(message = "Год издания должен быть положительным")
    private Year publicationYear;

    @NotNull(message = "Данные автора обязательны")
    private AuthorDto author;

    public BookDto() {
    }

    public BookDto(Long id, String title, String isbn, Year publicationYear, AuthorDto author) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Year getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Year publicationYear) {
        this.publicationYear = publicationYear;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }
}
