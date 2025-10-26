package com.example.pet.pet.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new PersonName(null));
        assertTrue(exception.getMessage().contains("Person name cannot be null or blank"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new PersonName(""));
        assertTrue(exception.getMessage().contains("Person name cannot be null or blank"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new PersonName("   "));
        assertTrue(exception.getMessage().contains("Person name cannot be null or blank"));
    }

    @Test
    void fromNullable_shouldReturnNullWhenValueIsNull() {
        PersonName name = PersonName.fromNullable(null);
        assertNull(name);
    }

    @Test
    void fromNullable_shouldReturnNullWhenValueIsBlank() {
        PersonName name = PersonName.fromNullable("   ");
        assertNull(name);
    }

    @Test
    void fromNullable_shouldCreatePersonNameWhenValueIsValid() {
        PersonName name = PersonName.fromNullable("John Doe");
        assertNotNull(name);
        assertEquals("John Doe", name.value());
    }
}
