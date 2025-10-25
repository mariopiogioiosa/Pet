package com.example.pet.pet.domain;

/**
 * Exception thrown when an optimistic lock conflict is detected.
 * This occurs when attempting to update an entity whose version has been modified by another transaction.
 */
public class OptimisticLockException extends RuntimeException {

    private final Long petId;
    private final Long expectedVersion;
    private final Long actualVersion;

    public OptimisticLockException(Long petId, Long expectedVersion, Long actualVersion) {
        super(String.format(
                "Optimistic lock failure for Pet with ID %d: expected version %d but found version %d",
                petId, expectedVersion, actualVersion
        ));
        this.petId = petId;
        this.expectedVersion = expectedVersion;
        this.actualVersion = actualVersion;
    }

    public Long getPetId() {
        return petId;
    }

    public Long getExpectedVersion() {
        return expectedVersion;
    }

    public Long getActualVersion() {
        return actualVersion;
    }
}
