package com.example.pet.pet.infrastructure.persistence;

import com.example.pet.pet.domain.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of PetRepository.
 * Useful for development, testing, and demos without requiring a real database.
 */
public class InMemoryPetRepository implements PetRepository {

    private final Map<Long, Pet> pets = new ConcurrentHashMap<>();

    public InMemoryPetRepository() {
        seedData();
    }

    private void seedData() {
        // Seed with fake data (moved from GetPetByIdHandler)
        Pet buddy = new Pet(
                1L,
                new PetName("Buddy"),
                new Species("Dog"),
                new Age(3),
                new PersonName("John Doe"),
                0L
        );
        pets.put(1L, buddy);
    }

    @Override
    public Optional<Pet> findById(Long id) {
        return Optional.ofNullable(pets.get(id));
    }
}
