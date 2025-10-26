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
        return repository.findById(id)
                .map(pet -> updatePet(id, request, pet))
                .map(PetDTO::fromPet);

    }

    private Pet updatePet(Long id, UpdatePetRequest request, Pet pet) {
        Pet updatedPet = new Pet(id,
                new PetName(request.name()),
                new Species(request.species()),
                Age.fromNullable(request.age()),
                PersonName.fromNullable(request.ownerName()),
                pet.getVersion()
        );

        repository.save(updatedPet);
        return updatedPet;
    }
}
