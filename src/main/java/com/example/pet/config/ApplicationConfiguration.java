package com.example.pet.config;

import com.example.pet.pet.application.CreatePetHandler;
import com.example.pet.pet.application.DeletePetHandler;
import com.example.pet.pet.application.GetAllPetsHandler;
import com.example.pet.pet.application.GetPetByIdHandler;
import com.example.pet.pet.application.UpdatePetHandler;
import com.example.pet.pet.domain.PetRepository;
import com.example.pet.pet.infrastructure.persistence.InMemoryPetRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public PetRepository petRepository() {
        return new InMemoryPetRepository();
    }

    @Bean
    public GetPetByIdHandler getPetByIdHandler(PetRepository petRepository) {
        return new GetPetByIdHandler(petRepository);
    }

    @Bean
    public GetAllPetsHandler getAllPetsHandler(PetRepository petRepository) {
        return new GetAllPetsHandler(petRepository);
    }

    @Bean
    public CreatePetHandler createPetHandler(PetRepository petRepository) {
        return new CreatePetHandler(petRepository);
    }

    @Bean
    public UpdatePetHandler updatePetHandler(PetRepository petRepository) {
        return new UpdatePetHandler(petRepository);
    }

    @Bean
    public DeletePetHandler deletePetHandler(PetRepository petRepository) {
        return new DeletePetHandler(petRepository);
    }
}
