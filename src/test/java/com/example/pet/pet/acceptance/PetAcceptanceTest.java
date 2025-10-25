package com.example.pet.pet.acceptance;

import com.example.pet.config.ApplicationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = com.example.pet.pet.infrastructure.api.PetController.class)
@Import(ApplicationConfiguration.class)
class PetAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndRetrievePet() throws Exception {
        // Create a pet
        String createRequest = """
                {
                    "name": "Buddy",
                    "species": "Dog",
                    "age": 3,
                    "ownerName": "John Doe"
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/v1/pets")
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
        mockMvc.perform(get("/api/v1/pets/" + createdPetId)
                        .contentType(MediaType.APPLICATION_JSON))
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
        mockMvc.perform(get("/api/v1/pets/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
