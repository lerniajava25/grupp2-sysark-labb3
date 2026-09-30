package org.example.grupp2sysarklabb3;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pets")
public class PetResource {
    private final petService petService;

    @Inject
   public PetResource(petService petService){
    this.petService = petService;
    }
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

