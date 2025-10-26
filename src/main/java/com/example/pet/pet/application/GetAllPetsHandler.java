package com.example.pet.pet.application;

import com.example.pet.pet.domain.PetRepository;
import java.util.List;

public class GetAllPetsHandler {

    private final PetRepository petRepository;

    public GetAllPetsHandler(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<PetDTO> handle() {
        return petRepository.findAll().stream().map(PetDTO::fromPet).toList();
    }
}
