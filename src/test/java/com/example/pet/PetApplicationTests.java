package com.example.pet;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pet.pet.application.CreatePetHandler;
import com.example.pet.pet.application.CreatePetRequest;
import com.example.pet.pet.application.GetPetByIdHandler;
import com.example.pet.pet.application.PetDTO;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PetApplicationTests {

    @Autowired private CreatePetHandler createHandler;

    @Autowired private GetPetByIdHandler getHandler;

    @Test
    void contextLoads() {}

    @Test
    void shouldCreateAndRetrievePet() {
        // Create a pet
        CreatePetRequest request = new CreatePetRequest("Buddy", "Dog", 3, "John Doe");

        PetDTO createdPet = createHandler.handle(request);

        // Verify creation
        assertThat(createdPet.id()).isNotNull();
        assertThat(createdPet.name()).isEqualTo("Buddy");
        assertThat(createdPet.species()).isEqualTo("Dog");
        assertThat(createdPet.age()).isEqualTo(3);
        assertThat(createdPet.ownerName()).isEqualTo("John Doe");

        // Retrieve the pet by ID
        Optional<PetDTO> retrievedPet = getHandler.handle(createdPet.id());

        // Verify retrieval
        assertThat(retrievedPet).isPresent();
        assertThat(retrievedPet.get().id()).isEqualTo(createdPet.id());
        assertThat(retrievedPet.get().name()).isEqualTo("Buddy");
        assertThat(retrievedPet.get().species()).isEqualTo("Dog");
    }
}
