package org.example.grupp2sysarklabb3;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PetDTO(@NotBlank String name,
                     @NotBlank String species,
                     @NotBlank @Min(0) @Max(100) int hungerLevel,
                     @NotBlank @Min(0) @Max(100) int happiness) { }
