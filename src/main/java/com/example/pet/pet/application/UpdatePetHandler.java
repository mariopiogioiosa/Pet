package com.example.pet.pet.application;

import com.example.pet.pet.domain.*;

import java.util.Optional;

/**
 * Command handler for updating an existing pet.
 * Fetches existing pet, creates updated instance, and persists it with optimistic locking.
 */
public class UpdatePetHandler {

    private final PetRepository repository;

    public UpdatePetHandler(PetRepository repository) {
        this.repository = repository;
    }

    public Optional<PetDTO> handle(Long id, UpdatePetRequest request) {
        Optional<Pet> existingPetOpt = repository.findById(id);

        if (existingPetOpt.isEmpty()) {
            return Optional.empty();
        }

        Pet existingPet = existingPetOpt.get();

        Pet updatedPet = new Pet(
                id,
                new PetName(request.name()),
                new Species(request.species()),
                Age.fromNullable(request.age()),
                PersonName.fromNullable(request.ownerName()),
                existingPet.getVersion()
        );

        Pet savedPet = repository.save(updatedPet);

        return Optional.of(PetDTO.fromPet(savedPet));
    }
}
