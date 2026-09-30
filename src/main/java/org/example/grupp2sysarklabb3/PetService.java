package org.example.grupp2sysarklabb3;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class PetService {
    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();

    public void adoptPet(PetDTO pet) {
        pets.put(1L, pet);
    }

    public Object[] listAllPets() {
        return pets.values().toArray();
    }

    public PetDTO viewPetStatus(Long petId) {
        return null;
    }

    public void feedPet(Long petId) {

    }

    public void playWithPet(Long petId) {

    }

    public void releasePet(Long petId) {

    }
}
