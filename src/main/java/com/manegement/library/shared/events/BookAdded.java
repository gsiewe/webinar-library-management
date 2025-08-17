package com.manegement.library.shared.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BookAdded extends DomainEvent {
    private String isbn;
    private String title;
    private String author;
    private String location;

    @Override
    public String getEventType() {
        return "BookAdded";
    }
}
