package com.example.pet.pet.infrastructure.api;

import com.example.pet.pet.application.GetPetByIdHandler;
import com.example.pet.pet.application.PetDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pets")
@Tag(name = "Pet", description = "Pet management APIs")
public class PetController {

    private final GetPetByIdHandler queryHandler;

    public PetController(GetPetByIdHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a pet by ID", description = "Returns a pet based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pet",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found",
                    content = @Content)
    })
    public ResponseEntity<PetDTO> getPetById(@Parameter(name = "id", description = "Pet ID", required = true, example = "123")
                                                 @PathVariable Long id) {
        return queryHandler.handle(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

}
