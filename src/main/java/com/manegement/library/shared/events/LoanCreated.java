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
public class LoanCreated extends DomainEvent {
    private String loanId;
    private String userId;
    private String userEmail;
    private String isbn;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    @Override
    public String getEventType() {
        return "LoanCreated";
    }
}
