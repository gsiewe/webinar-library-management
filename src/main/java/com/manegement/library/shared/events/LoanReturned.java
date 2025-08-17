package com.manegement.library.shared.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LoanReturned extends DomainEvent {
    private String loanId;
    private String userId;
    private String isbn;
    private LocalDate returnDate;
    private boolean wasOverdue;
    private Integer daysOverdue;

    @Override
    public String getEventType() {
        return "LoanReturned";
    }
}
