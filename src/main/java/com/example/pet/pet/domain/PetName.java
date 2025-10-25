package com.example.pet.pet.domain;

public record PetName(String value) {

    public PetName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Pet name cannot be null or blank");
        }
    }
}
