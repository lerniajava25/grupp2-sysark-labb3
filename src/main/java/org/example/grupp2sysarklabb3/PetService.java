package org.example.grupp2sysarklabb3;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.ws.rs.NotFoundException;
import java.util.concurrent.locks.ReentrantLock;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class PetService {
    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    private final ReentrantLock lock = new ReentrantLock();

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
        PetDTO pet = pets.get(petId);

        if (pet == null) {
            throw new NotFoundException("Status can't be shown: ID: " + petId + " not found");
        }

        return pet;
    }

    public void feedPet(Long petId) {
        lock.lock();

        try {
            PetDTO pet = pets.get(petId);
            if (pet == null) {
                throw new NotFoundException("Cannot feed pet: ID: " + petId + " not found");
            }

            int currentHungerLevel = pet.hungerLevel();
            int newHungerLevel = Math.max(0, currentHungerLevel - 10);

            PetDTO newPet = new PetDTO(
                    pet.id(),
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
        lock.lock();
        try {
            PetDTO pet = pets.get(petId);
            if (pet == null) {
                throw new NotFoundException("Pet not found");

            }

            int newHappiness = Math.min(100, pet.happiness() + 15);
            pets.put(petId, new PetDTO(
                    pet.id(),
                    pet.name(),
                    pet.species(),
                    pet.hungerLevel(),
                    newHappiness
            ));
        } finally {
            lock.unlock();
          }
        }
        public PetPageResponse listPets(int page,int size){
        List<PetDTO> allPets = pets.values().stream()
                .sorted(Comparator.comparing(PetDTO::id))
                .toList();

            long offset = (long) page * size;
            int fromIndex = (int) Math.min(offset, allPets.size());
            int toIndex = Math.min(fromIndex * size, allPets.size());

            List<PetDTO> pageOfPets = allPets.subList(fromIndex, toIndex);
            int totalPages = (int) Math.ceil((double) allPets.size() / size);

            return new PetPageResponse(
                    pageOfPets,
                    page,
                    size,
                    allPets.size(),
                    totalPages
            );

        }


    public void releasePet(Long petId) {
        if (!pets.containsKey(petId)) {
            throw new NotFoundException("Pet not found");
        }
        pets.remove(petId);
    }
}
