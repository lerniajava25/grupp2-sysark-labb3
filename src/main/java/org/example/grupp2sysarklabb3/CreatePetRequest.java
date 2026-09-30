package org.example.grupp2sysarklabb3;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePetRequest(@NotBlank String name,
                               @NotBlank String species,
                               @NotNull @Min(0) @Max(100) Integer hungerLevel,
                               @NotNull @Min(0) @Max(100) Integer happiness) {
}
