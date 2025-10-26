package com.example.pet.pet.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for updating an existing pet.
 */
@Schema(description = "Request to update an existing pet")
public record UpdatePetRequest(
        @NotBlank(message = "Pet name is required")
        @Schema(description = "Name of the pet", example = "Buddy", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotBlank(message = "Species is required")
        @Schema(description = "Species of the pet", example = "Dog", requiredMode = Schema.RequiredMode.REQUIRED)
        String species,

        @Min(value = 0, message = "Age must be non-negative")
        @Schema(description = "Age of the pet in years", example = "3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer age,

        @Schema(description = "Name of the pet's owner", example = "John Doe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String ownerName
) {
}
