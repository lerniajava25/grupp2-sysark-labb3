package org.example.grupp2sysarklabb3;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

    @PUT
    @Path("/{id}/play")
    @Produces(MediaType.APPLICATION_JSON)
    public Response playWithPet(@PathParam("id") Long id) {
        PetDTO pet = petService.viewPetStatus(id);

        if (pet == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Pet not found")
                    .build();
        }

        petService.playWithPet(id);

        return Response.ok(petService.viewPetStatus(id))
                .build();
    }
}
