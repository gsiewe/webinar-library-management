package com.manegement.library.shared.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookAdded.class, name = "BookAdded"),
        @JsonSubTypes.Type(value = BookAvailabilityChanged.class, name = "BookAvailabilityChanged"),
        @JsonSubTypes.Type(value = LoanCreated.class, name = "LoanCreated"),
        @JsonSubTypes.Type(value = LoanReturned.class, name = "LoanReturned"),
        @JsonSubTypes.Type(value = LoanOverdue.class, name = "LoanOverdue"),
        @JsonSubTypes.Type(value = FineCreated.class, name = "FineCreated")
})
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class DomainEvent {
    private String eventId;
    private LocalDateTime occurredOn;
    private String aggregateId;

    protected DomainEvent(String aggregateId) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
        this.aggregateId = aggregateId;
    }

    public abstract String getEventType();
}