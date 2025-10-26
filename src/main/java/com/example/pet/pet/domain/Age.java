package com.example.pet.pet.domain;

public record Age(int value) {

    public Age {
        if (value < 0) {
            throw new IllegalArgumentException(
                    "Age must be greater than or equal to 0, but was: " + value);
        }
    }

    /**
     * Factory method to create an Age from a nullable Integer.
     *
     * @param value the age value, can be null
     * @return an Age instance if value is not null, otherwise null
     */
    public static Age fromNullable(Integer value) {
        return value != null ? new Age(value) : null;
    }
}
