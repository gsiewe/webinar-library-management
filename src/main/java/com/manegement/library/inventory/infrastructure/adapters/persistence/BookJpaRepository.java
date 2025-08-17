package com.manegement.library.inventory.infrastructure.adapters.persistence;

import com.manegement.library.inventory.domain.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookJpaRepository extends JpaRepository<BookJpaEntity, String> {
    Optional<BookJpaEntity> findByIsbn(String isbn);
    List<BookJpaEntity> findByStatus(BookStatus status);
    List<BookJpaEntity> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    boolean existsByIsbn(String isbn);
}
