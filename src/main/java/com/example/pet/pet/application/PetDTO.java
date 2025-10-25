package com.example.pet.pet.application;

public record PetDTO(
        Long id,
        String name,
        String species,
        Integer age,
        String ownerName
) {
}
