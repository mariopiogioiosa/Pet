package com.example.pet.pet.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetNameTest {

    @Test
    void shouldCreatePetNameWithValidValue() {
        PetName name = new PetName("Buddy");
        assertEquals("Buddy", name.value());
    }

    @Test
    void shouldCreatePetNameWithSingleCharacter() {
        PetName name = new PetName("M");
        assertEquals("M", name.value());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PetName(null)
        );
        assertTrue(exception.getMessage().contains("Pet name cannot be null or blank"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PetName("")
        );
        assertTrue(exception.getMessage().contains("Pet name cannot be null or blank"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PetName("   ")
        );
        assertTrue(exception.getMessage().contains("Pet name cannot be null or blank"));
    }
}
