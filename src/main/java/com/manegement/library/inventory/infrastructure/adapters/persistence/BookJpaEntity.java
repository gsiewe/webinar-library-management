package com.manegement.library.inventory.infrastructure.adapters.persistence;

import com.manegement.library.inventory.domain.model.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookJpaEntity {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String publisher;

    private Integer publicationYear;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    private LocalDateTime lastStatusChange;

    private String lastBorrowerId;
}
