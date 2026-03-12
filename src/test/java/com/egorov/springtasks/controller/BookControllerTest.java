package com.egorov.springtasks.controller;

import com.egorov.springtasks.dto.AuthorDto;
import com.egorov.springtasks.dto.BookDto;
import com.egorov.springtasks.exception.BookNotFoundException;
import com.egorov.springtasks.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.time.Year;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @Autowired
    private ObjectMapper objectMapper;
    private AuthorDto authorDto;
    private BookDto firstTestBook;
    private BookDto secondTestBook;

    @BeforeEach
    void setUp() {
        authorDto = new AuthorDto(1L, "Егор Егоров");

        firstTestBook = new BookDto(
                1L,
                "Егор и мир",
                "978-5-17-123456-7",
                Year.of(2026),
                authorDto
        );

        secondTestBook = new BookDto(
                2L,
                "Егор и море",
                "978-5-17-654321-0",
                Year.of(2022),
                authorDto
        );
    }

    @Test
    void getAllBooksWithPages() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        List<BookDto> books = List.of(firstTestBook, secondTestBook);
        Page<BookDto> page = new PageImpl<>(books, pageable, 2);

        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/books").param("page", "0").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Егор и мир"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title").value("Егор и море"))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(bookService, times(1)).getAllBooks(any(Pageable.class));
    }

    @Test
    void getBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(firstTestBook);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Егор и мир"))
                .andExpect(jsonPath("$.isbn").value("978-5-17-123456-7"))
                .andExpect(jsonPath("$.publicationYear").value(2026))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.name").value("Егор Егоров"));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void createBook() throws Exception {

        BookDto newBook = new BookDto();
        newBook.setTitle("Кавказский пленник");
        newBook.setIsbn("978-5-17-999999-9");
        newBook.setPublicationYear(Year.of(1872));

        AuthorDto newAuthor = new AuthorDto();
        newAuthor.setName("Лев Толстой");
        newBook.setAuthor(newAuthor);

        BookDto createdBook = new BookDto(
                3L,
                "Кавказский пленник",
                "978-5-17-999999-9",
                Year.of(1872),
                new AuthorDto(1L, "Лев Толстой")
        );

        when(bookService.createBook(any(BookDto.class))).thenReturn(createdBook);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Кавказский пленник"))
                .andExpect(jsonPath("$.author.name").value("Лев Толстой"));

        verify(bookService, times(1)).createBook(any(BookDto.class));
    }

    @Test
    void updateBook() throws Exception {
        BookDto updateDto = new BookDto();
        updateDto.setTitle("Война и мир (Том 1)");

        BookDto updatedBook = new BookDto(
                1L,
                "Война и мир (Том 1)",
                "978-5-17-123456-7",
                Year.of(1869),
                authorDto
        );

        when(bookService.updateBook(eq(1L), any(BookDto.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Война и мир (Том 1)"));

        verify(bookService, times(1)).updateBook(eq(1L), any(BookDto.class));
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void deleteFantomBook() throws Exception {
        doThrow(new BookNotFoundException("Not Found")).when(bookService).deleteBook(999L);

        mockMvc.perform(delete("/books/999"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).deleteBook(999L);
    }
}