package com.manegement.library.shared.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BookAvailabilityChanged extends DomainEvent {
    private String isbn;
    private String previousStatus;
    private String newStatus;
    private String reason; // "LOAN_CREATED", "LOAN_RETURNED", "RESERVATION", etc.

    @Override
    public String getEventType() {
        return "BookAvailabilityChanged";
    }
}
