package com.manegement.library.inventory.infrastructure.adapters.messaging;

import com.manegement.library.inventory.domain.model.Book;
import com.manegement.library.inventory.domain.model.BookStatus;
import com.manegement.library.inventory.domain.ports.outbound.InventoryEventPublisher;
import com.manegement.library.shared.events.BookAdded;
import com.manegement.library.shared.events.BookAvailabilityChanged;
import com.manegement.library.shared.infrastructure.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventPublisherAdapter implements InventoryEventPublisher {

    private final EventPublisher eventPublisher;

    @Value("${aws.sns.topics.inventory}")
    private String inventoryTopic;

    @Override
    public void publishBookAdded(Book book) {
        BookAdded event = BookAdded.builder()
                .aggregateId(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .location(book.getLocation())
                .build();

        eventPublisher.publish(inventoryTopic, event);
        log.info("Published BookAdded event for ISBN: {}", book.getIsbn());
    }

    @Override
    public void publishBookAvailabilityChanged(String isbn, BookStatus oldStatus, BookStatus newStatus, String reason) {
        BookAvailabilityChanged event = BookAvailabilityChanged.builder()
                .aggregateId(isbn)
                .isbn(isbn)
                .previousStatus(oldStatus.name())
                .newStatus(newStatus.name())
                .reason(reason)
                .build();

        eventPublisher.publish(inventoryTopic, event);
        log.info("Published BookAvailabilityChanged event for ISBN: {} ({} -> {})",
                isbn, oldStatus, newStatus);
    }
}
