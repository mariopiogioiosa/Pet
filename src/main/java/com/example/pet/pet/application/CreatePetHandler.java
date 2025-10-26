package com.example.pet.pet.application;

import com.example.pet.pet.domain.*;

public class CreatePetHandler {

    private final PetRepository repository;

    public CreatePetHandler(PetRepository repository) {
        this.repository = repository;
    }

    public PetDTO handle(CreatePetRequest request) {
        Pet pet =
                new Pet(
                        new PetName(request.name()),
                        new Species(request.species()),
                        Age.fromNullable(request.age()),
                        PersonName.fromNullable(request.ownerName()));

        Pet savedPet = repository.save(pet);

        return PetDTO.fromPet(savedPet);
    }
}
