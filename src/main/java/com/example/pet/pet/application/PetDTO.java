package com.example.pet.pet.application;

import com.example.pet.pet.domain.Pet;

public record PetDTO(Long id, String name, String species, Integer age, String ownerName) {
    public static PetDTO fromPet(Pet pet) {
        return new PetDTO(
                pet.getId(),
                pet.getName().value(),
                pet.getSpecies().value(),
                pet.getAge() != null ? pet.getAge().value() : null,
                pet.getOwnerName() != null ? pet.getOwnerName().value() : null);
    }
}
