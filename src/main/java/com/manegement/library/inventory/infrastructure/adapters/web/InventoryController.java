package com.manegement.library.inventory.infrastructure.adapters.web;

import com.manegement.library.inventory.domain.model.Book;
import com.manegement.library.inventory.domain.ports.inbound.InventoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryUseCase inventoryUseCase;

    @PostMapping("/books")
    public ResponseEntity<BookResponse> addBook(@RequestBody AddBookRequest request) {
        Book book = inventoryUseCase.addNewBook(
                new InventoryUseCase.AddBookCommand(
                        request.isbn(),
                        request.title(),
                        request.author(),
                        request.publisher(),
                        request.publicationYear(),
                        request.location()
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(BookResponse.from(book));
    }

    @GetMapping("/books/{isbn}")
    public ResponseEntity<BookResponse> getBook(@PathVariable String isbn) {
        return inventoryUseCase.findByIsbn(isbn)
                .map(book -> ResponseEntity.ok(BookResponse.from(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/books/{isbn}/availability")
    public ResponseEntity<AvailabilityResponse> checkAvailability(@PathVariable String isbn) {
        boolean available = inventoryUseCase.isBookAvailable(isbn);
        return ResponseEntity.ok(new AvailabilityResponse(isbn, available));
    }

    @GetMapping("/books/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {
        List<Book> books = inventoryUseCase.findAvailableBooks();
        List<BookResponse> response = books.stream()
                .map(BookResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/search")
    public ResponseEntity<List<BookResponse>> searchBooks(@RequestParam String query) {
        List<Book> books = inventoryUseCase.searchBooks(query);
        List<BookResponse> response = books.stream()
                .map(BookResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    // DTOs
    record AddBookRequest(
            String isbn,
            String title,
            String author,
            String publisher,
            Integer publicationYear,
            String location
    ) {}

    record BookResponse(
            String id,
            String isbn,
            String title,
            String author,
            String publisher,
            Integer publicationYear,
            String location,
            String status,
            boolean available
    ) {
        static BookResponse from(Book book) {
            return new BookResponse(
                    book.getId(),
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublisher(),
                    book.getPublicationYear(),
                    book.getLocation(),
                    book.getStatus().name(),
                    book.isAvailable()
            );
        }
    }

    record AvailabilityResponse(String isbn, boolean available) {}
}
