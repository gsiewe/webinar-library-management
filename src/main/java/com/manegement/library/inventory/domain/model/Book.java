package com.manegement.library.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer publicationYear;
    private String location; // Campus, Étagère
    private BookStatus status;
    private LocalDateTime lastStatusChange;
    private String lastBorrowerId;

    // Domain logic
    public static Book createNew(String isbn, String title, String author,
                                 String publisher, Integer publicationYear, String location) {
        return Book.builder()
                .id(UUID.randomUUID().toString())
                .isbn(isbn)
                .title(title)
                .author(author)
                .publisher(publisher)
                .publicationYear(publicationYear)
                .location(location)
                .status(BookStatus.AVAILABLE)
                .lastStatusChange(LocalDateTime.now())
                .build();
    }

    public void markAsBorrowed(String borrowerId) {
        if (this.status != BookStatus.AVAILABLE) {
            throw new IllegalStateException(
                    String.format("Book %s cannot be borrowed. Current status: %s", isbn, status)
            );
        }
        this.status = BookStatus.BORROWED;
        this.lastBorrowerId = borrowerId;
        this.lastStatusChange = LocalDateTime.now();
    }

    public void markAsReturned() {
        if (this.status != BookStatus.BORROWED) {
            throw new IllegalStateException(
                    String.format("Book %s cannot be returned. Current status: %s", isbn, status)
            );
        }
        this.status = BookStatus.AVAILABLE;
        this.lastStatusChange = LocalDateTime.now();
    }

    public void markAsReserved() {
        if (this.status != BookStatus.AVAILABLE) {
            throw new IllegalStateException(
                    String.format("Book %s cannot be reserved. Current status: %s", isbn, status)
            );
        }
        this.status = BookStatus.RESERVED;
        this.lastStatusChange = LocalDateTime.now();
    }

    public boolean isAvailable() {
        return this.status == BookStatus.AVAILABLE;
    }
}
