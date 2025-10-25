package com.example.pet.config;

import com.example.pet.application.handlers.GetPetByIdQueryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public GetPetByIdQueryHandler getPetByIdQueryHandler() {
        return new GetPetByIdQueryHandler();
    }
}
