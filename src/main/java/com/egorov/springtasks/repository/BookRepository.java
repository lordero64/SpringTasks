package com.egorov.springtasks.repository;

import com.egorov.springtasks.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    boolean existsByIsbn(String isbn);
    Page<Book> findAll(Pageable pageable);
}
