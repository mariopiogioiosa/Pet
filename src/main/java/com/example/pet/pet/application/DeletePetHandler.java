package com.example.pet.pet.application;

import com.example.pet.pet.domain.PetRepository;

/**
 * Command handler for deleting an existing pet.
 * Returns true if the pet was deleted, false if not found.
 */
public class DeletePetHandler {

    private final PetRepository repository;

    public DeletePetHandler(PetRepository repository) {
        this.repository = repository;
    }

    public boolean handle(Long id) {
        return repository.deleteById(id);
    }
}
