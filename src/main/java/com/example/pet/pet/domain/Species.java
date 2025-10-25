package com.example.pet.pet.domain;

public record Species(String value) {

    public Species {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Species cannot be null or blank");
        }
    }
}
