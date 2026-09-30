package org.example.grupp2sysarklabb3;

import java.util.concurrent.ConcurrentHashMap;

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

    }

    public void releasePet(Long petId) {

    }
}
