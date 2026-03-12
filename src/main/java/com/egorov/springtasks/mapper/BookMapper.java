package com.egorov.springtasks.mapper;

import com.egorov.springtasks.dto.AuthorDto;
import com.egorov.springtasks.dto.BookDto;
import com.egorov.springtasks.entity.Author;
import com.egorov.springtasks.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {
    public Book toEntity(BookDto dto) {
        if (dto == null) return null;

        Author author = new Author(dto.getAuthor().getName());

        return new Book(dto.getTitle(), dto.getIsbn(), dto.getPublicationYear(), author);
    }

    public BookDto toDto(Book book) {
        if (book == null) return null;

        AuthorDto authorDto = new AuthorDto(
                book.getAuthor().getId(),
                book.getAuthor().getName()
        );

        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationYear(),
                authorDto
        );
    }

    public void updateEntity(Book book, BookDto dto) {
        if (dto == null) return;

        if (dto.getTitle() != null) book.setTitle(dto.getTitle());
        if (dto.getIsbn() != null) book.setIsbn(dto.getIsbn());
        if (dto.getPublicationYear() != null) book.setPublicationYear(dto.getPublicationYear());

        if (dto.getAuthor() != null && dto.getAuthor().getName() != null) {
            book.getAuthor().setName(dto.getAuthor().getName());
        }
    }

    public Book toEntity(BookDto dto, Author author) {
        if (dto == null) return null;

        return new Book(
                dto.getTitle(),
                dto.getIsbn(),
                dto.getPublicationYear(),
                author
        );
    }

    public List<BookDto> toDtoList(List<Book> books) {
        return books.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Page<BookDto> toDtoPage(Page<Book> booksPage) {
        List<BookDto> dtos = toDtoList(booksPage.getContent());
        return new PageImpl<>(dtos, booksPage.getPageable(), booksPage.getTotalElements());
    }
}
