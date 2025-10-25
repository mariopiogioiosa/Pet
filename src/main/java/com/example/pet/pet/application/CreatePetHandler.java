package com.example.pet.pet.application;

import com.example.pet.pet.domain.*;

/**
 * Command handler for creating a new pet.
 * Converts request DTO to domain model, persists it, and returns DTO.
 */
public class CreatePetHandler {

    private final PetRepository repository;

    public CreatePetHandler(PetRepository repository) {
        this.repository = repository;
    }

    public PetDTO handle(CreatePetRequest request) {
        Pet pet = new Pet(
                new PetName(request.name()),
                new Species(request.species()),
                request.age() != null ? new Age(request.age()) : null,
                request.ownerName() != null && !request.ownerName().isBlank()
                        ? new PersonName(request.ownerName())
                        : null
        );

        Pet savedPet = repository.save(pet);

        return toDTO(savedPet);
    }

    private PetDTO toDTO(Pet pet) {
        return new PetDTO(
                pet.getId(),
                pet.getName().value(),
                pet.getSpecies().value(),
                pet.getAge() != null ? pet.getAge().value() : null,
                pet.getOwnerName() != null ? pet.getOwnerName().value() : null
        );
    }
}
