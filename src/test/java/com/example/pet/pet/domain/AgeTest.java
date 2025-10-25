package com.example.pet.pet.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgeTest {

    @Test
    void shouldCreateAgeWithZero() {
        Age age = new Age(0);
        assertEquals(0, age.value());
    }

    @Test
    void shouldCreateAgeWithPositiveValue() {
        Age age = new Age(5);
        assertEquals(5, age.value());
    }


    @Test
    void shouldThrowExceptionWhenAgeIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Age(-1)
        );
        assertTrue(exception.getMessage().contains("Age must be greater than or equal to 0"));
    }

}
