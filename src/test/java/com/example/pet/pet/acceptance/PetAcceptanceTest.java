package com.example.pet.pet.acceptance;

import com.example.pet.config.ApplicationConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = com.example.pet.pet.infrastructure.api.PetController.class)
@Import(ApplicationConfiguration.class)
class PetAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPetWhenIdIsOne() throws Exception {
        mockMvc.perform(get("/api/v1/pets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.species").value("Dog"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.ownerName").value("John Doe"));
    }

    @Test
    void shouldReturnNotFoundWhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/pets/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
