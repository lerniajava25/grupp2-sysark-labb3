package org.example.grupp2sysarklabb3;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

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
    public List<PetDTO> getPets() {
        return petService.listAllPets();
    }

    @GET
    @Path("/page")
    @Produces(MediaType.APPLICATION_JSON)
    public PetPageResponse geyPetsPage(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        if (page < 0 || size < 1 || size < 100){
            throw new BadRequestException(
                    "page must be 0 or greater; size must be between 1 and 100");
        }
        return petService.listPets(page, size);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPet(@Valid CreatePetRequest req, @Context UriInfo uriInfo) {
        PetDTO pet = petService.adoptPet(req);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(pet.id())).build();
        return Response.created(location).entity(pet).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewPetStatus(@PathParam("id") Long id) {
        PetDTO pet = petService.viewPetStatus(id);
        return Response.ok(pet).build();
    }

    @PUT
    @Path("/{id}/feed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response feedPet(@PathParam("id") Long id) {
        petService.feedPet(id);
        return Response.ok(petService.viewPetStatus(id)).build();
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


    @DELETE
    @Path("/{id}")
    public Response releasePet(@PathParam("id") Long id){
    PetDTO pet = petService.viewPetStatus(id);

    if (pet == null){
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Pet not found")
                .build();
    }

    petService.releasePet(id);

    return Response.noContent().build();

    }
}

