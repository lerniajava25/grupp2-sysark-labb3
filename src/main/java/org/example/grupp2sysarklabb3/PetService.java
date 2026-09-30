package org.example.grupp2sysarklabb3;

import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class PetService {
    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();

    public void adoptPet(Long petId) {

    }

    public PetDTO[] listAllPets() {
        return new PetDTO[0];
    }

    public PetDTO viewPetStatus(Long petId) {
        return null;
    }

    public void feedPet(Long petId) {

    }

    public void playWithPet(Long petId) {
        PetDTO pet = pets.get(petId);
        if (pet == null) {
            throw new NotFoundException("Pet not found");
        }

        int newHappiness = Math.min(100,pet.happiness() + 15);
        pets.put(petId, new PetDTO(
                pet.name(),
                pet.species(),
                pet.hungerLevel(),
                newHappiness
        ));

    }

    public void releasePet(Long petId) {
        if (!pets.containsKey(petId)) {
            throw new NotFoundException("Pet not found");
        }
        pets.remove(petId);
    }
}
