package com.example.pet.pet.infrastructure.persistence;

import com.example.pet.pet.domain.PetRepository;
import com.example.pet.pet.domain.PetRepositoryContractTest;

import java.util.List;

/**
 * Tests for InMemoryPetRepository.
 * Extends the contract test to ensure compliance with PetRepository specification.
 */
class InMemoryPetRepositoryTest extends PetRepositoryContractTest {

    @Override
    public PetRepository repoWithNoData() {
        return new InMemoryPetRepository(List.of());
    }

    @Override
    public PetRepository repoWithData() {
        return new InMemoryPetRepository(List.of(BUDDY, MAX));
    }
}
