package com.example.pet.pet.domain;

public record Age(int value) {

    public Age {
        if (value < 0) {
            throw new IllegalArgumentException("Age must be greater than or equal to 0, but was: " + value);
        }
    }
}
