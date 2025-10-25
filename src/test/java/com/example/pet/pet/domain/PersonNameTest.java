package com.example.pet.pet.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonNameTest {

    @Test
    void shouldCreatePersonNameWithValidValue() {
        PersonName name = new PersonName("John Doe");
        assertEquals("John Doe", name.value());
    }

    @Test
    void shouldCreatePersonNameWithSingleCharacter() {
        PersonName name = new PersonName("J");
        assertEquals("J", name.value());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PersonName(null)
        );
        assertTrue(exception.getMessage().contains("Person name cannot be null or blank"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PersonName("")
        );
        assertTrue(exception.getMessage().contains("Person name cannot be null or blank"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PersonName("   ")
        );
        assertTrue(exception.getMessage().contains("Person name cannot be null or blank"));
    }
}
