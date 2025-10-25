package com.example.pet.config;

import com.example.pet.pet.application.GetPetByIdHandler;
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
}
