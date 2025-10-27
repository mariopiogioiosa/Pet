package com.example.pet.pet.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.pet.config.ApplicationConfiguration;
import com.example.pet.infrastructure.web.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = com.example.pet.pet.infrastructure.api.PetController.class)
@Import({ApplicationConfiguration.class, GlobalExceptionHandler.class})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class PetAcceptanceTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndRetrievePet() throws Exception {
        // Create a pet
        String createRequest =
                """
                {
                    "name": "Buddy",
                    "species": "Dog",
                    "age": 3,
                    "ownerName": "John Doe"
                }
                """;

        MvcResult createResult =
                mockMvc.perform(
                                post("/api/v1/pets")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(createRequest))
                        .andExpect(status().isCreated())
                        .andExpect(header().exists("Location"))
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.name").value("Buddy"))
                        .andExpect(jsonPath("$.species").value("Dog"))
                        .andExpect(jsonPath("$.age").value(3))
                        .andExpect(jsonPath("$.ownerName").value("John Doe"))
                        .andReturn();

        // Extract the created pet's ID from the response
        String responseBody = createResult.getResponse().getContentAsString();
        Long createdPetId = objectMapper.readTree(responseBody).get("id").asLong();

        // Retrieve the pet by ID
        mockMvc.perform(get("/api/v1/pets/" + createdPetId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdPetId))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.species").value("Dog"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.ownerName").value("John Doe"));
    }

    @Test
    void shouldReturnNotFoundWhenPetDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/pets/999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdatePetSuccessfully() throws Exception {
        // Create a pet first
        String createRequest =
                """
                {
                    "name": "Max",
                    "species": "Cat",
                    "age": 2,
                    "ownerName": "Jane Smith"
                }
                """;

        MvcResult createResult =
                mockMvc.perform(
                                post("/api/v1/pets")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(createRequest))
                        .andExpect(status().isCreated())
                        .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        Long createdPetId = objectMapper.readTree(responseBody).get("id").asLong();

        // Update the pet
        String updateRequest =
                """
                {
                    "name": "Maximus",
                    "species": "Cat",
                    "age": 3,
                    "ownerName": "Jane Doe"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/pets/" + createdPetId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdPetId))
                .andExpect(jsonPath("$.name").value("Maximus"))
                .andExpect(jsonPath("$.species").value("Cat"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.ownerName").value("Jane Doe"));

        // Verify the update persisted by fetching the pet
        mockMvc.perform(get("/api/v1/pets/" + createdPetId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maximus"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.ownerName").value("Jane Doe"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPet() throws Exception {
        String updateRequest =
                """
                {
                    "name": "Ghost",
                    "species": "Dog",
                    "age": 5,
                    "ownerName": "Nobody"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/pets/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeletePetSuccessfully() throws Exception {
        // Create a pet first
        String createRequest =
                """
                {
                    "name": "Charlie",
                    "species": "Dog",
                    "age": 4,
                    "ownerName": "Bob Smith"
                }
                """;

        MvcResult createResult =
                mockMvc.perform(
                                post("/api/v1/pets")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(createRequest))
                        .andExpect(status().isCreated())
                        .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        Long createdPetId = objectMapper.readTree(responseBody).get("id").asLong();

        // Delete the pet
        mockMvc.perform(delete("/api/v1/pets/" + createdPetId)).andExpect(status().isNoContent());

        // Verify the pet no longer exists
        mockMvc.perform(get("/api/v1/pets/" + createdPetId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentPet() throws Exception {
        mockMvc.perform(delete("/api/v1/pets/999")).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnEmptyListWhenNoPetsExist() throws Exception {
        mockMvc.perform(get("/api/v1/pets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnProblemDetailsWhenValidationFails() throws Exception {
        // Attempt to create a pet with multiple validation violations
        String invalidRequest =
                """
                {
                    "name": "",
                    "species": "",
                    "age": -5,
                    "ownerName": "John Doe"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").value("/api/v1/pets"))
                // Verify detailed validation errors are included
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(3))
                // Check that validation messages contain expected text
                .andExpect(
                        jsonPath("$.errors[?(@.field == 'name')].message")
                                .value("Pet name is required"))
                .andExpect(
                        jsonPath("$.errors[?(@.field == 'species')].message")
                                .value("Species is required"))
                .andExpect(
                        jsonPath("$.errors[?(@.field == 'age')].message")
                                .value("Age must be non-negative"));
    }

    @Test
    void shouldReturnProblemDetailsWhenUpdateValidationFails() throws Exception {
        // Create a valid pet first
        String createRequest =
                """
                {
                    "name": "Buddy",
                    "species": "Dog",
                    "age": 3,
                    "ownerName": "John Doe"
                }
                """;

        MvcResult createResult =
                mockMvc.perform(
                                post("/api/v1/pets")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(createRequest))
                        .andExpect(status().isCreated())
                        .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        long createdPetId = objectMapper.readTree(responseBody).get("id").asLong();

        // Attempt to update with invalid data
        String invalidUpdateRequest =
                """
                {
                    "name": "",
                    "species": "",
                    "age": -10,
                    "ownerName": "Jane Doe"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/pets/" + createdPetId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidUpdateRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").value("/api/v1/pets/" + createdPetId))
                // Verify detailed validation errors are included
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(3))
                // Check that validation messages contain expected text
                .andExpect(
                        jsonPath("$.errors[?(@.field == 'name')].message")
                                .value("Pet name is required"))
                .andExpect(
                        jsonPath("$.errors[?(@.field == 'species')].message")
                                .value("Species is required"))
                .andExpect(
                        jsonPath("$.errors[?(@.field == 'age')].message")
                                .value("Age must be non-negative"));
    }

    @Test
    void shouldReturnAllPetsWhenMultiplePetsExist() throws Exception {
        // Create first pet
        String createRequest1 =
                """
                {
                    "name": "Buddy",
                    "species": "Dog",
                    "age": 3,
                    "ownerName": "John Doe"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createRequest1))
                .andExpect(status().isCreated());

        // Create second pet
        String createRequest2 =
                """
                {
                    "name": "Whiskers",
                    "species": "Cat",
                    "age": 2,
                    "ownerName": "Jane Smith"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createRequest2))
                .andExpect(status().isCreated());

        // Create third pet without optional fields
        String createRequest3 =
                """
                {
                    "name": "Goldie",
                    "species": "Fish"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createRequest3))
                .andExpect(status().isCreated());

        // Get all pets and verify
        mockMvc.perform(get("/api/v1/pets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Buddy"))
                .andExpect(jsonPath("$[0].species").value("Dog"))
                .andExpect(jsonPath("$[0].age").value(3))
                .andExpect(jsonPath("$[0].ownerName").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Whiskers"))
                .andExpect(jsonPath("$[1].species").value("Cat"))
                .andExpect(jsonPath("$[1].age").value(2))
                .andExpect(jsonPath("$[1].ownerName").value("Jane Smith"))
                .andExpect(jsonPath("$[2].name").value("Goldie"))
                .andExpect(jsonPath("$[2].species").value("Fish"))
                .andExpect(jsonPath("$[2].age").isEmpty())
                .andExpect(jsonPath("$[2].ownerName").isEmpty());
    }
}
