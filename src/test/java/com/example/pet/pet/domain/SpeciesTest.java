package com.example.pet.pet.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeciesTest {

    @Test
    void shouldCreateSpeciesWithValidValue() {
        Species species = new Species("Dog");
        assertEquals("Dog", species.value());
    }

    @Test
    void shouldCreateSpeciesWithDifferentAnimalTypes() {
        assertEquals("Cat", new Species("Cat").value());
        assertEquals("Rabbit", new Species("Rabbit").value());
        assertEquals("Monkey", new Species("Monkey").value());
    }

    @Test
    void shouldThrowExceptionWhenSpeciesIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Species(null)
        );
        assertTrue(exception.getMessage().contains("Species cannot be null or blank"));
    }

    @Test
    void shouldThrowExceptionWhenSpeciesIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Species("")
        );
        assertTrue(exception.getMessage().contains("Species cannot be null or blank"));
    }

    @Test
    void shouldThrowExceptionWhenSpeciesIsBlank() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Species("   ")
        );
        assertTrue(exception.getMessage().contains("Species cannot be null or blank"));
    }

    @Test
    void shouldBeEqualWhenValuesAreTheSame() {
        Species dog1 = new Species("Dog");
        Species dog2 = new Species("Dog");
        assertEquals(dog1, dog2);
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        Species dog = new Species("Dog");
        Species cat = new Species("Cat");
        assertNotEquals(dog, cat);
    }
}
