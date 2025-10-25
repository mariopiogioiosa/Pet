package com.example.pet.pet.application;

import java.util.Optional;

public class GetPetByIdHandler {

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
