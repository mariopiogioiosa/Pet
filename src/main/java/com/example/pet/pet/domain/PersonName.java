package com.example.pet.pet.domain;

public record PersonName(String value) {

    public PersonName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Person name cannot be null or blank");
        }
    }

    /**
     * Factory method to create a PersonName from a nullable String.
     *
     * @param value the person name, can be null or blank
     * @return a PersonName instance if value is not null and not blank, otherwise null
     */
    public static PersonName fromNullable(String value) {
        return value != null && !value.isBlank() ? new PersonName(value) : null;
    }
}
