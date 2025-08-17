package com.manegement.library.inventory.domain.ports.inbound;


import com.manegement.library.inventory.domain.model.Book;
import com.manegement.library.inventory.domain.model.BookStatus;

import java.util.List;
import java.util.Optional;

public interface InventoryUseCase {
    // Commands
    Book addNewBook(AddBookCommand command);
    void updateBookStatus(String isbn, BookStatus newStatus, String reason);
    void handleBookBorrowed(String isbn, String borrowerId);
    void handleBookReturned(String isbn);

    // Queries
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAvailableBooks();
    List<Book> searchBooks(String searchTerm);
    boolean isBookAvailable(String isbn);

    // DTOs
    record AddBookCommand(
            String isbn,
            String title,
            String author,
            String publisher,
            Integer publicationYear,
            String location
    ) {}
}
