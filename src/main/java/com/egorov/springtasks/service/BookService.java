package com.egorov.springtasks.service;

import com.egorov.springtasks.dto.AuthorDto;
import com.egorov.springtasks.dto.BookDto;
import com.egorov.springtasks.entity.Author;
import com.egorov.springtasks.entity.Book;
import com.egorov.springtasks.exception.BookNotFoundException;
import com.egorov.springtasks.exception.DuplicateIsbnException;
import com.egorov.springtasks.exception.ResourceNotFoundException;
import com.egorov.springtasks.mapper.BookMapper;
import com.egorov.springtasks.repository.AuthorRepository;
import com.egorov.springtasks.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional(readOnly = true)
    public Page<BookDto> getAllBooks (Pageable pageable){
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return bookMapper.toDtoPage(booksPage);
    }

    @Transactional(readOnly = true)
    public BookDto getBookById(Long id) {
        Book book = findBookById(id);
        return bookMapper.toDto(book);
    }

    public BookDto createBook(BookDto bookDto) {

        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new DuplicateIsbnException("Книга с ISBN " + bookDto.getIsbn() + " уже существует");
        }

        Author author = findOrCreateAuthor(bookDto.getAuthor());

        // 3. Маппер только преобразует
        Book book = bookMapper.toEntity(bookDto, author);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = findBookById(id);

        if (bookDto.getIsbn() != null &&
                !bookDto.getIsbn().equals(book.getIsbn()) &&
                bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new DuplicateIsbnException("Книга с ISBN " + bookDto.getIsbn() + " уже существует");
        }

        if (bookDto.getAuthor() != null && bookDto.getAuthor().getName() != null) {
            Author author = findOrCreateAuthor(bookDto.getAuthor());
            book.setAuthor(author);
        }

        bookMapper.updateEntity(book, bookDto);
        Book updatedBook = bookRepository.save(book);

        return bookMapper.toDto(updatedBook);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга не найдена с id: " + id));
        bookRepository.delete(book);
    }

    private Author findOrCreateAuthor(AuthorDto authorDto) {
        if (authorDto == null) {
            throw new IllegalArgumentException("Автор не может быть null");
        }

        // Если есть ID, ищем по ID
        if (authorDto.getId() != null) {
            return authorRepository.findById(authorDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Автор не найден с id: " + authorDto.getId()));
        }

        // Иначе ищем по имени
        return authorRepository.findByName(authorDto.getName())
                .orElseGet(() -> {
                    Author newAuthor = new Author(authorDto.getName());
                    return authorRepository.save(newAuthor);
                });
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с id: " + id));
    }
}
