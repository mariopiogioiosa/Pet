package com.example.pet.pet.domain;

/**
 * Exception thrown when an optimistic lock conflict is detected.
 * This occurs when attempting to update an entity whose version has been modified by another transaction.
 */
public class OptimisticLockException extends RuntimeException {

    public OptimisticLockException(Long petId, Long expectedVersion, Long actualVersion) {
        super(
                String.format(
                        "Optimistic lock failure for Pet with ID %d: expected version %d but found"
                                + " version %d",
                        petId, expectedVersion, actualVersion));
    }
}
