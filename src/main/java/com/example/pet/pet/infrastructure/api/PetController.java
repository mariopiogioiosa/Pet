package com.example.pet.pet.infrastructure.api;

import com.example.pet.pet.application.CreatePetHandler;
import com.example.pet.pet.application.CreatePetRequest;
import com.example.pet.pet.application.GetPetByIdHandler;
import com.example.pet.pet.application.PetDTO;
import com.example.pet.pet.application.UpdatePetHandler;
import com.example.pet.pet.application.UpdatePetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/pets")
@Tag(name = "Pet", description = "Pet management APIs")
public class PetController {

    private final GetPetByIdHandler queryHandler;
    private final CreatePetHandler createHandler;
    private final UpdatePetHandler updateHandler;

    public PetController(
            GetPetByIdHandler queryHandler,
            CreatePetHandler createHandler,
            UpdatePetHandler updateHandler) {
        this.queryHandler = queryHandler;
        this.createHandler = createHandler;
        this.updateHandler = updateHandler;
    }

    @PostMapping
    @Operation(
            summary = "Create a new pet",
            description = "Creates a new pet and returns it with an assigned ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Pet created successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = PetDTO.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input data",
                        content = @Content)
            })
    public ResponseEntity<PetDTO> createPet(@Valid @RequestBody CreatePetRequest request) {
        PetDTO createdPet = createHandler.handle(request);

        // Build Location header: /api/v1/pets/{id}
        URI location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(createdPet.id())
                        .toUri();

        return ResponseEntity.created(location).body(createdPet);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a pet by ID", description = "Returns a pet based on the provided ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved pet",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = PetDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Pet not found",
                        content = @Content)
            })
    public ResponseEntity<PetDTO> getPetById(
            @Parameter(name = "id", description = "Pet ID", required = true, example = "123")
                    @PathVariable
                    Long id) {
        return queryHandler
                .handle(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing pet",
            description = "Updates a pet with new information")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Pet updated successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = PetDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Pet not found",
                        content = @Content),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input data",
                        content = @Content)
            })
    public ResponseEntity<PetDTO> updatePet(
            @Parameter(name = "id", description = "Pet ID", required = true, example = "123")
                    @PathVariable
                    Long id,
            @Valid @RequestBody UpdatePetRequest request) {
        return updateHandler
                .handle(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}
