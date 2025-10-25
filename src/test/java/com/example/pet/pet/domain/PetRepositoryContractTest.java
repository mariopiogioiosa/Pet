package com.example.pet.pet.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Contract test for PetRepository implementations.
 * All implementations must extend this class to ensure consistent behavior.
 */
public abstract class PetRepositoryContractTest {

    protected static final Pet BUDDY = new Pet(
            1L,
            new PetName("Buddy"),
            new Species("Dog"),
            new Age(3),
            new PersonName("John Doe"),
            0L
    );

    protected static final Pet MAX = new Pet(
            2L,
            new PetName("Max"),
            new Species("Cat"),
            new Age(5),
            new PersonName("Jane Smith"),
            0L
    );

    @Test
    void findById_shouldReturnEmptyWhenPetNotFound() {
        PetRepository repository = repoWithNoData();

        Optional<Pet> result = repository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findById_shouldReturnPetWhenFound() {
        PetRepository repository = repoWithData();

        Optional<Pet> result = repository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo(new PetName("Buddy"));
        assertThat(result.get().getSpecies()).isEqualTo(new Species("Dog"));
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoPets() {
        PetRepository repository = repoWithNoData();

        List<Pet> pets = repository.findAll();

        assertThat(pets).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllPets() {
        PetRepository repository = repoWithData();

        List<Pet> pets = repository.findAll();

        assertThat(pets).hasSize(2);
        assertThat(pets).extracting(Pet::getId).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void save_shouldCreateNewPetWithIdAssigned() {
        PetRepository repository = repoWithNoData();
        Pet newPet = new Pet(
                new PetName("Charlie"),
                new Species("Rabbit"),
                new Age(2),
                new PersonName("Alice")
        );

        Pet saved = repository.save(newPet);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(new PetName("Charlie"));
        assertThat(saved.getVersion()).isEqualTo(0L);

        // Verify it's retrievable
        Optional<Pet> retrieved = repository.findById(saved.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo(new PetName("Charlie"));
    }

    @Test
    void save_shouldUpdateExistingPet() {
        PetRepository repository = repoWithData();
        Pet existingPet = repository.findById(1L).orElseThrow();

        // Create updated version of the pet
        Pet updatedPet = new Pet(
                existingPet.getId(),
                new PetName("Buddy Updated"),
                existingPet.getSpecies(),
                existingPet.getAge(),
                existingPet.getOwnerName(),
                existingPet.getVersion()
        );

        Pet saved = repository.save(updatedPet);

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getName()).isEqualTo(new PetName("Buddy Updated"));
    }

    @Test
    void save_shouldIncrementVersionOnUpdate() {
        PetRepository repository = repoWithData();
        Pet existingPet = repository.findById(1L).orElseThrow();
        Long initialVersion = existingPet.getVersion();

        // Create updated version
        Pet updatedPet = new Pet(
                existingPet.getId(),
                new PetName("Buddy v2"),
                existingPet.getSpecies(),
                existingPet.getAge(),
                existingPet.getOwnerName(),
                initialVersion
        );

        Pet saved = repository.save(updatedPet);

        assertThat(saved.getVersion()).isEqualTo(initialVersion + 1);

        // Verify version persisted
        Pet retrieved = repository.findById(1L).orElseThrow();
        assertThat(retrieved.getVersion()).isEqualTo(initialVersion + 1);
    }

    @Test
    void deleteById_shouldReturnTrueWhenPetExists() {
        PetRepository repository = repoWithData();

        boolean deleted = repository.deleteById(1L);

        assertThat(deleted).isTrue();
        assertThat(repository.findById(1L)).isEmpty();
    }

    @Test
    void deleteById_shouldReturnFalseWhenPetNotFound() {
        PetRepository repository = repoWithNoData();

        boolean deleted = repository.deleteById(999L);

        assertThat(deleted).isFalse();
    }

    @Test
    void deleteById_shouldBeIdempotent() {
        PetRepository repository = repoWithData();

        boolean firstDelete = repository.deleteById(1L);
        boolean secondDelete = repository.deleteById(1L);

        assertThat(firstDelete).isTrue();
        assertThat(secondDelete).isFalse();
    }

    @Test
    void save_shouldThrowOptimisticLockExceptionWhenVersionMismatch() {
        PetRepository repository = repoWithData();
        Pet existingPet = repository.findById(1L).orElseThrow();

        // Create update with stale version (version 0)
        Pet staleUpdate = new Pet(
                existingPet.getId(),
                new PetName("Buddy Modified"),
                existingPet.getSpecies(),
                existingPet.getAge(),
                existingPet.getOwnerName(),
                0L  // Stale version
        );

        // First update - should succeed (version 0 matches)
        Pet firstUpdate = repository.save(staleUpdate);
        assertThat(firstUpdate.getVersion()).isEqualTo(1L);

        // Second update with same stale version - should fail
        assertThatThrownBy(() -> repository.save(staleUpdate))
                .isInstanceOf(OptimisticLockException.class)
                .hasMessageContaining("Optimistic lock failure")
                .hasMessageContaining("ID 1")
                .hasMessageContaining("expected version 0")
                .hasMessageContaining("found version 1");
    }

    @Test
    void save_shouldPreventConcurrentModifications() {
        PetRepository repository = repoWithData();

        // Simulate two users reading the same pet
        Pet userAVersion = repository.findById(1L).orElseThrow();
        Pet userBVersion = repository.findById(1L).orElseThrow();

        // Both have version 0
        assertThat(userAVersion.getVersion()).isEqualTo(0L);
        assertThat(userBVersion.getVersion()).isEqualTo(0L);

        // User A updates successfully
        Pet userAUpdate = new Pet(
                userAVersion.getId(),
                new PetName("User A Update"),
                userAVersion.getSpecies(),
                userAVersion.getAge(),
                userAVersion.getOwnerName(),
                userAVersion.getVersion()
        );
        Pet savedByUserA = repository.save(userAUpdate);
        assertThat(savedByUserA.getVersion()).isEqualTo(1L);

        // User B tries to update with stale version - should fail
        Pet userBUpdate = new Pet(
                userBVersion.getId(),
                new PetName("User B Update"),
                userBVersion.getSpecies(),
                userBVersion.getAge(),
                userBVersion.getOwnerName(),
                userBVersion.getVersion()  // Still version 0 (stale)
        );

        assertThatThrownBy(() -> repository.save(userBUpdate))
                .isInstanceOf(OptimisticLockException.class)
                .extracting("petId", "expectedVersion", "actualVersion")
                .containsExactly(1L, 0L, 1L);
    }

    @Test
    void save_shouldAllowUpdateWithCorrectVersion() {
        PetRepository repository = repoWithData();

        // First update
        Pet pet = repository.findById(1L).orElseThrow();
        Pet update1 = new Pet(
                pet.getId(),
                new PetName("Update 1"),
                pet.getSpecies(),
                pet.getAge(),
                pet.getOwnerName(),
                pet.getVersion()
        );
        Pet saved1 = repository.save(update1);
        assertThat(saved1.getVersion()).isEqualTo(1L);

        // Second update with correct version (1)
        Pet update2 = new Pet(
                saved1.getId(),
                new PetName("Update 2"),
                saved1.getSpecies(),
                saved1.getAge(),
                saved1.getOwnerName(),
                saved1.getVersion()  // Correct version: 1
        );
        Pet saved2 = repository.save(update2);
        assertThat(saved2.getVersion()).isEqualTo(2L);

        // Verify final state
        Pet finalPet = repository.findById(1L).orElseThrow();
        assertThat(finalPet.getName()).isEqualTo(new PetName("Update 2"));
        assertThat(finalPet.getVersion()).isEqualTo(2L);
    }

    /**
     * Factory method to create a repository with no data.
     * @return empty repository
     */
    public abstract PetRepository repoWithNoData();

    /**
     * Factory method to create a repository with test data (BUDDY and MAX).
     * @return repository containing BUDDY and MAX
     */
    public abstract PetRepository repoWithData();
}
