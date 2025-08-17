package com.manegement.library.inventory.domain.ports.outbound;

import com.manegement.library.inventory.domain.model.Book;
import com.manegement.library.inventory.domain.model.BookStatus;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findById(String id);
    List<Book> findByStatus(BookStatus status);
    List<Book> findByTitleContainingOrAuthorContaining(String title, String author);
    boolean existsByIsbn(String isbn);
    List<Book> findAll();
}
