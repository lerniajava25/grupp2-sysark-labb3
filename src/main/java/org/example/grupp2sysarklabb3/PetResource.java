package org.example.grupp2sysarklabb3;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/pets")
public class PetResource {
    private PetService petService;

    public PetResource() {

    }

    @Inject
    public PetResource(PetService petService) {
        this.petService = petService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object[] getPets() {
        return petService.listAllPets();
    }

    @POST
    public void postPet(String name, String species, int hunger, int happiness) {
        petService.adoptPet(new PetDTO(name, species, hunger, happiness));
    }
}
