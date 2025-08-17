package com.manegement.library.inventory.domain.ports.outbound;

import com.manegement.library.inventory.domain.model.Book;
import com.manegement.library.inventory.domain.model.BookStatus;

public interface InventoryEventPublisher {
    void publishBookAdded(Book book);
    void publishBookAvailabilityChanged(String isbn, BookStatus oldStatus, BookStatus newStatus, String reason);
}
