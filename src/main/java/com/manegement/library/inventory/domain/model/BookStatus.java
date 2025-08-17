package com.manegement.library.inventory.domain.model;

public enum BookStatus {
    AVAILABLE("Disponible"),
    BORROWED("Emprunté"),
    RESERVED("Réservé"),
    MAINTENANCE("En maintenance"),
    LOST("Perdu");

    private final String displayName;

    BookStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
