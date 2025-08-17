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
public class LoanOverdue extends DomainEvent {
    private String loanId;
    private String userId;
    private String userEmail;
    private String isbn;
    private String bookTitle;
    private LocalDate dueDate;
    private Integer daysOverdue;
    private BigDecimal suggestedFineAmount;

    @Override
    public String getEventType() {
        return "LoanOverdue";
    }
}
