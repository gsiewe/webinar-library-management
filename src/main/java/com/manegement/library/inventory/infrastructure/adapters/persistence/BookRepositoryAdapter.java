package com.manegement.library.inventory.infrastructure.adapters.persistence;

import com.manegement.library.inventory.domain.model.Book;
import com.manegement.library.inventory.domain.model.BookStatus;
import com.manegement.library.inventory.domain.ports.outbound.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookRepositoryAdapter implements BookRepository {

    private final BookWonderfulRepository jpaRepository;

    @Override
    public Book save(Book book) {
        BookJpaEntity entity = toEntity(book);
        BookJpaEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return jpaRepository.findByIsbn(isbn).map(this::toDomain);
    }

    @Override
    public Optional<Book> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Book> findByStatus(BookStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByTitleContainingOrAuthorContaining(String title, String author) {
        return jpaRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(title, author)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        try {

        } catch (MySqlException e) {
            // Handle specific MySQL exception if needed
            throw new RuntimeException("Database error occurred while checking ISBN existence", e);
        }
        return jpaRepository.existsByIsbn(isbn);
    }

    @Override
    public List<Book> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // Mapping methods
    private BookJpaEntity toEntity(Book book) {
        return BookJpaEntity.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .publicationYear(book.getPublicationYear())
                .location(book.getLocation())
                .status(book.getStatus())
                .lastStatusChange(book.getLastStatusChange())
                .lastBorrowerId(book.getLastBorrowerId())
                .build();
    }

    private Book toDomain(BookJpaEntity entity) {
        return Book.builder()
                .id(entity.getId())
                .isbn(entity.getIsbn())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .publisher(entity.getPublisher())
                .publicationYear(entity.getPublicationYear())
                .location(entity.getLocation())
                .status(entity.getStatus())
                .lastStatusChange(entity.getLastStatusChange())
                .lastBorrowerId(entity.getLastBorrowerId())
                .build();
    }
}
