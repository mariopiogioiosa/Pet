package com.example.pet.pet.application;

import com.example.pet.pet.domain.Pet;
import com.example.pet.pet.domain.PetRepository;

import java.util.Optional;

public class GetPetByIdHandler {

    private final PetRepository repository;

    public GetPetByIdHandler(PetRepository repository) {
        this.repository = repository;
    }

    public Optional<PetDTO> handle(Long id) {
        return repository.findById(id)
                .map(PetDTO::fromPet);
    }
}
