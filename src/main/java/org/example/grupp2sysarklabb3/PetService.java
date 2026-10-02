package org.example.grupp2sysarklabb3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@ApplicationScoped
public class PetService {
    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private final ReentrantLock lock = new ReentrantLock();

    public PetDTO adoptPet(CreatePetRequest req) {
        lock.lock();
        try {
            long id = nextId.getAndIncrement();
            PetDTO pet = new PetDTO(
                    id,
                    req.name(),
                    req.species(),
                    req.hungerLevel(),
                    req.happiness()
            );
            pets.put(id, pet);
            return pet;
        } finally {
            lock.unlock();
        }
    }

    public List<PetDTO> listAllPets() {
        lock.lock();
        try {
            return pets.values().stream()
                    .sorted(Comparator.comparing(PetDTO::id))
                    .toList();
        } finally {
            lock.unlock();
        }
    }

    public PetDTO viewPetStatus(Long petId) {
        PetDTO pet = pets.get(petId);
        if (pet == null) {
            throw new NotFoundException("Pet with ID " + petId + " not found");
        }
        return pet;
    }

    public void feedPet(Long petId) {
        lock.lock();
        try {
            PetDTO pet = requirePet(petId);
            int newHungerLevel = Math.max(0, pet.hungerLevel() - 10);
            pets.put(petId, new PetDTO(
                    pet.id(),
                    pet.name(),
                    pet.species(),
                    newHungerLevel,
                    pet.happiness()
            ));
        } finally {
            lock.unlock();
        }
    }

    public void playWithPet(Long petId) {
        lock.lock();
        try {
            PetDTO pet = requirePet(petId);
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

    public PetPageResponse listPets(int page, int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new BadRequestException(
                    "page must be 0 or greater; size must be between 1 and 100"
            );
        }

        lock.lock();
        try {
            List<PetDTO> allPets = pets.values().stream()
                    .sorted(Comparator.comparing(PetDTO::id))
                    .toList();

            long offset = (long) page * size;
            int fromIndex = (int) Math.min(offset, allPets.size());
            int toIndex = (int) Math.min(offset + size, allPets.size());
            List<PetDTO> pageOfPets = allPets.subList(fromIndex, toIndex);
            int totalPages = (int) Math.ceil((double) allPets.size() / size);

            return new PetPageResponse(
                    pageOfPets,
                    page,
                    size,
                    allPets.size(),
                    totalPages
            );
        } finally {
            lock.unlock();
        }
    }

    public void releasePet(Long petId) {
        lock.lock();
        try {
            if (pets.remove(petId) == null) {
                throw new NotFoundException("Pet with ID " + petId + " not found");
            }
        } finally {
            lock.unlock();
        }
    }

    private PetDTO requirePet(Long petId) {
        PetDTO pet = pets.get(petId);
        if (pet == null) {
            throw new NotFoundException("Pet with ID " + petId + " not found");
        }
        return pet;
    }
}
