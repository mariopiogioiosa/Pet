package com.example.pet.pet.domain;

import java.util.List;
import java.util.Optional;

public interface PetRepository {

    /**
     * Finds a pet by its unique identifier.
     *
     * @param id the pet's ID
     * @return an Optional containing the pet if found, or empty if not found
     */
    Optional<Pet> findById(Long id);

    /**
     * Saves a pet (handles both create and update).
     * If the pet has no ID, it will be created and assigned a new ID.
     * If the pet has an ID, it will be updated.
     *
     * @param pet the pet to save
     * @return the saved pet with ID assigned (if it was new)
     */
    Pet save(Pet pet);

    /**
     * Deletes a pet by its unique identifier.
     *
     * @param id the pet's ID to delete
     * @return true if the pet was deleted, false if not found
     */
    boolean deleteById(Long id);

    /**
     * Finds all pets.
     *
     * @return a list of all pets (empty list if none found)
     */
    List<Pet> findAll();
}
