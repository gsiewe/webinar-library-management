package com.manegement.library.inventory.application;

import com.manegement.library.inventory.domain.exceptions.BookNotFound;
import com.manegement.library.inventory.domain.model.Book;
import com.manegement.library.inventory.domain.model.BookStatus;
import com.manegement.library.inventory.domain.ports.inbound.InventoryUseCase;
import com.manegement.library.inventory.domain.ports.outbound.BookRepository;
import com.manegement.library.inventory.domain.ports.outbound.InventoryEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService implements InventoryUseCase {

    private final BookRepository bookRepository;
    private final InventoryEventPublisher eventPublisher;

    @Override
    public Book addNewBook(AddBookCommand command) {
        log.info("Adding new book with ISBN: {}", command.isbn());

        // Vérifier si le livre existe déjà
        if (bookRepository.existsByIsbn(command.isbn())) {
            throw new IllegalArgumentException("Book with ISBN " + command.isbn() + " already exists");
        }

        // Créer le livre avec la logique métier
        Book book = Book.createNew(
                command.isbn(),
                command.title(),
                command.author(),
                command.publisher(),
                command.publicationYear(),
                command.location()
        );

        // Sauvegarder
        Book savedBook = bookRepository.save(book);

        // Publier l'événement
        eventPublisher.publishBookAdded(savedBook);

        log.info("Book added successfully: {}", savedBook.getIsbn());
        return savedBook;
    }

    @Override
    public void updateBookStatus(String isbn, BookStatus newStatus, String reason) {
        log.info("Updating book {} status to {}", isbn, newStatus);

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFound("Book not found: " + isbn));

        BookStatus oldStatus = book.getStatus();

        // Appliquer la logique métier selon le nouveau statut
        switch (newStatus) {
            case BORROWED -> book.markAsBorrowed(reason);
            case AVAILABLE -> book.markAsReturned();
            case RESERVED -> book.markAsReserved();
            default -> throw new IllegalArgumentException("Status change not supported: " + newStatus);
        }

        bookRepository.save(book);
        eventPublisher.publishBookAvailabilityChanged(isbn, oldStatus, newStatus, reason);
    }

    @Override
    public void handleBookBorrowed(String isbn, String borrowerId) {
        log.info("Handling book borrowed: {} by user: {}", isbn, borrowerId);
        updateBookStatus(isbn, BookStatus.BORROWED, borrowerId);
    }

    @Override
    public void handleBookReturned(String isbn) {
        log.info("Handling book returned: {}", isbn);
        updateBookStatus(isbn, BookStatus.AVAILABLE, "RETURNED");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAvailableBooks() {
        return bookRepository.findByStatus(BookStatus.AVAILABLE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> searchBooks(String searchTerm) {
        return bookRepository.findByTitleContainingOrAuthorContaining(searchTerm, searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookAvailable(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(Book::isAvailable)
                .orElse(false);
    }
}
