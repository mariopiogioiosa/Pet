package com.example.pet.application.handlers;

import com.example.pet.dto.PetDTO;

import java.util.Optional;

public class GetPetByIdQueryHandler {

    public Optional<PetDTO> handle(Long id) {
        if (id == 1L) {
            return Optional.of(new PetDTO(
                    1L,
                    "Buddy",
                    "Dog",
                    3,
                    "John Doe"
            ));
        }

        return Optional.empty();
    }
}
