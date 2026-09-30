package org.example.grupp2sysarklabb3;

import jakarta.ws.rs.NotFoundException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class PetService {
    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public void adoptPet(Long petId) {

    }

    public PetDTO[] listAllPets() {
        return new PetDTO[0];
    }

    public PetDTO viewPetStatus(Long petId) {
        PetDTO pet = pets.get(petId);

        if (pet == null){
            throw new NotFoundException("Status can't be shown: ID: " + petId + " not found");
        }

        return pet;
    }

    public void feedPet(Long petId) {
        lock.lock();

        try {
            PetDTO pet = pets.get(petId);

            if (pet == null){
                throw new NotFoundException("Cannot feed pet: ID: " + petId + " not found");
            }

            int currentHungerLevel = pet.hungerLevel();

            int newHungerLevel = Math.max(0, currentHungerLevel - 10);

            PetDTO newPet = new PetDTO(
                    pet.name(),
                    pet.species(),
                    newHungerLevel,
                    pet.happiness()
            );

            pets.put(petId, newPet);

        } finally {
            lock.unlock();
        }
    }

    public void playWithPet(Long petId) {

    }

    public void releasePet(Long petId) {

    }
}
