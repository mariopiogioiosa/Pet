package com.example.pet.dto;

public record PetDTO(
        Long id,
        String name,
        String species,
        Integer age,
        String ownerName
) {
}
