package com.example.pet.config;

import com.example.pet.pet.application.GetPetByIdHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public GetPetByIdHandler getPetByIdHandler() {
        return new GetPetByIdHandler();
    }
}
