package com.manegement.library.shared.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FineCreated extends DomainEvent {
    private String fineId;
    private String userId;
    private String loanId;
    private BigDecimal amount;
    private String reason;
    private LocalDate createdDate;

    @Override
    public String getEventType() {
        return "FineCreated";
    }
}
