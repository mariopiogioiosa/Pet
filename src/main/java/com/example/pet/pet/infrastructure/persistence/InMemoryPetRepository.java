package com.example.pet.pet.infrastructure.persistence;

import com.example.pet.pet.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of PetRepository.
 * Useful for development, testing, and demos without requiring a real database.
 */
public class InMemoryPetRepository implements PetRepository {

    private final Map<Long, Pet> pets = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public InMemoryPetRepository() {
        // No seed data - pets are created via the API
    }

    public InMemoryPetRepository(List<Pet> initialPets) {
        initialPets.forEach(pet -> pets.put(pet.getId(), pet));
    }

    @Override
    public Optional<Pet> findById(Long id) {
        return Optional.ofNullable(pets.get(id));
    }

    @Override
    public Pet save(Pet pet) {
        if (pet.getId() == null) {
            return createPet(pet);
        } else {
            return updatePet(pet);
        }
    }

    private Pet updatePet(Pet pet) {
        // Find existing pet to validate version
        Pet existingPet = pets.get(pet.getId());
        if (existingPet == null) {
            throw new IllegalArgumentException(
                    "Cannot update non-existent pet with ID: " + pet.getId());
        }

        // Validate optimistic lock - version must match
        if (!existingPet.getVersion().equals(pet.getVersion())) {
            throw new OptimisticLockException(
                    pet.getId(), pet.getVersion(), existingPet.getVersion());
        }

        // Version matches - proceed with update
        Pet updatedPet =
                new Pet(
                        pet.getId(),
                        pet.getName(),
                        pet.getSpecies(),
                        pet.getAge(),
                        pet.getOwnerName(),
                        pet.getVersion() + 1);
        pets.put(pet.getId(), updatedPet);
        return updatedPet;
    }

    private Pet createPet(Pet pet) {
        Long newId = idGenerator.getAndIncrement();
        Pet persistedPet =
                new Pet(
                        newId,
                        pet.getName(),
                        pet.getSpecies(),
                        pet.getAge(),
                        pet.getOwnerName(),
                        0L);
        pets.put(newId, persistedPet);
        return persistedPet;
    }

    @Override
    public boolean deleteById(Long id) {
        return pets.remove(id) != null;
    }

    @Override
    public List<Pet> findAll() {
        return new ArrayList<>(pets.values());
    }
}
