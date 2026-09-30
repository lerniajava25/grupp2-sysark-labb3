package org.example.grupp2sysarklabb3;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.ws.rs.NotFoundException;


@ApplicationScoped
public class PetService {
    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public PetDTO adoptPet(CreatePetRequest req) {
        long id = nextId.getAndIncrement();
        PetDTO pet = new PetDTO(id, req.name(), req.species(), req.hungerLevel(), req.happiness());
        pets.put(id, pet);
        return pet;
    }

    public List<PetDTO> listAllPets() {
        return List.copyOf(pets.values());
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
                pet.id(),
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
