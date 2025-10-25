package com.example.pet.pet.domain;

public record PersonName(String value) {

    public PersonName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Person name cannot be null or blank");
        }
    }
}
