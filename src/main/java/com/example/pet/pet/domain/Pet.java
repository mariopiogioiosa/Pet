package com.example.pet.pet.domain;

import java.util.Objects;

public class Pet {

    private Long id;
    private final PetName name;
    private final Species species;
    private final Age age;
    private final PersonName ownerName;
    private Long version;

    /**
     * Constructor for creating a new pet (not yet persisted).
     * ID will be assigned by the repository after persistence.
     */
    public Pet(PetName name, Species species, Age age, PersonName ownerName) {
        this.id = null;
        this.name = Objects.requireNonNull(name, "Name is required");
        this.species = Objects.requireNonNull(species, "Species is required");
        this.age = age;  // Optional
        this.ownerName = ownerName;  // Optional
        this.version = 0L;
    }

    /**
     * Constructor for loading a pet from persistence.
     */
    public Pet(Long id, PetName name, Species species, Age age, PersonName ownerName, Long version) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name is required");
        this.species = Objects.requireNonNull(species, "Species is required");
        this.age = age;
        this.ownerName = ownerName;
        this.version = version;
    }

    /**
     * Assigns an ID to this pet. Can only be called once by the repository after persistence.
     * Package-private to restrict access to infrastructure layer.
     */
    void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot reassign ID. Pet already has ID: " + this.id);
        }
        this.id = Objects.requireNonNull(id, "ID cannot be null");
    }

    /**
     * Increments the version for optimistic locking.
     * Package-private to restrict access to infrastructure layer.
     */
    void incrementVersion() {
        this.version++;
    }

    public Long getId() {
        return id;
    }

    public PetName getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public Age getAge() {
        return age;
    }

    public PersonName getOwnerName() {
        return ownerName;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name=" + name +
                ", species=" + species +
                ", age=" + age +
                ", ownerName=" + ownerName +
                ", version=" + version +
                '}';
    }
}
