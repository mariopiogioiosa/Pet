package com.example.pet.pet.domain;

import java.util.Optional;

public interface PetRepository {

    /**
     * Finds a pet by its unique identifier.
     *
     * @param id the pet's ID
     * @return an Optional containing the pet if found, or empty if not found
     */
    Optional<Pet> findById(Long id);
}
