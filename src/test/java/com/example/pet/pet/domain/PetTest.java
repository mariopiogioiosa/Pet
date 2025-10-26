package com.example.pet.pet.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PetTest {

    @Test
    void shouldCreateNewPetWithAllFields() {
        PetName name = new PetName("Buddy");
        Species species = new Species("Dog");
        Age age = new Age(3);
        PersonName ownerName = new PersonName("John");

        Pet pet = new Pet(name, species, age, ownerName);

        assertNull(pet.getId(), "New pet should not have ID yet");
        assertEquals(name, pet.getName());
        assertEquals(species, pet.getSpecies());
        assertEquals(age, pet.getAge());
        assertEquals(ownerName, pet.getOwnerName());
        assertEquals(0L, pet.getVersion());
    }

    @Test
    void shouldCreateNewPetWithOnlyRequiredFields() {
        PetName name = new PetName("Max");
        Species species = new Species("Cat");

        Pet pet = new Pet(name, species, null, null);

        assertNull(pet.getId());
        assertEquals(name, pet.getName());
        assertEquals(species, pet.getSpecies());
        assertNull(pet.getAge());
        assertNull(pet.getOwnerName());
        assertEquals(0L, pet.getVersion());
    }

    @Test
    void shouldCreatePetFromPersistence() {
        Long id = 42L;
        PetName name = new PetName("Charlie");
        Species species = new Species("Rabbit");
        Age age = new Age(2);
        PersonName ownerName = new PersonName("Alice");
        Long version = 5L;

        Pet pet = new Pet(id, name, species, age, ownerName, version);

        assertEquals(id, pet.getId());
        assertEquals(name, pet.getName());
        assertEquals(species, pet.getSpecies());
        assertEquals(age, pet.getAge());
        assertEquals(ownerName, pet.getOwnerName());
        assertEquals(version, pet.getVersion());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Species species = new Species("Dog");

        NullPointerException exception =
                assertThrows(NullPointerException.class, () -> new Pet(null, species, null, null));
        assertTrue(exception.getMessage().contains("Name is required"));
    }

    @Test
    void shouldThrowExceptionWhenSpeciesIsNull() {
        PetName name = new PetName("Buddy");

        NullPointerException exception =
                assertThrows(NullPointerException.class, () -> new Pet(name, null, null, null));
        assertTrue(exception.getMessage().contains("Species is required"));
    }

    @Test
    void shouldAssignIdWhenPetHasNoId() {
        Pet pet =
                new Pet(
                        new PetName("Buddy"),
                        new Species("Dog"),
                        new Age(3),
                        new PersonName("John"));

        assertNull(pet.getId());

        pet.assignId(100L);

        assertEquals(100L, pet.getId());
    }

    @Test
    void shouldThrowExceptionWhenReassigningId() {
        Pet pet = new Pet(new PetName("Buddy"), new Species("Dog"), null, null);

        pet.assignId(100L);

        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () -> pet.assignId(200L));
        assertTrue(exception.getMessage().contains("Cannot reassign ID"));
        assertTrue(exception.getMessage().contains("100"));
    }

    @Test
    void shouldThrowExceptionWhenAssigningNullId() {
        Pet pet = new Pet(new PetName("Buddy"), new Species("Dog"), null, null);

        NullPointerException exception =
                assertThrows(NullPointerException.class, () -> pet.assignId(null));
        assertTrue(exception.getMessage().contains("ID cannot be null"));
    }

    @Test
    void shouldIncrementVersion() {
        Pet pet = new Pet(new PetName("Buddy"), new Species("Dog"), null, null);

        assertEquals(0L, pet.getVersion());

        pet.incrementVersion();
        assertEquals(1L, pet.getVersion());

        pet.incrementVersion();
        assertEquals(2L, pet.getVersion());
    }

    @Test
    void shouldHaveReadableToString() {
        Pet pet =
                new Pet(
                        1L,
                        new PetName("Buddy"),
                        new Species("Dog"),
                        new Age(3),
                        new PersonName("John"),
                        0L);

        String toString = pet.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("Buddy"));
        assertTrue(toString.contains("Dog"));
        assertTrue(toString.contains("age="));
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("version=0"));
    }
}
