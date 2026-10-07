package org.example.grupp2sysarklabb3;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
public class PetResource {
    @Inject
    private PetService petService;

    public PetResource() {
    }

    @GET
    public List<PetDTO> getPets(
            @QueryParam("species") String species,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("order") @DefaultValue("asc") String order) {

        if (species != null && !species.isBlank()) {
            return petService.listFilteredPets(species);
        }

        if (sortBy != null && !sortBy.isBlank()) {
            return petService.listAllPetsSorted(sortBy, order);
        }

        return petService.listAllPets();
    }

    @GET
    @Path("/page")
    public PetPageResponse getPetsPage(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        if (page < 0 || size < 1 || size > 100) {
            throw new BadRequestException(
                    "page must be 0 or greater; size must be between 1 and 100"
            );
        }
        return petService.listPets(page, size);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPet(@Valid CreatePetRequest req, @Context UriInfo uriInfo) {
        PetDTO pet = petService.adoptPet(req);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(pet.id()))
                .build();
        return Response.created(location).entity(pet).build();
    }

    @GET
    @Path("/{id}")
    public PetDTO viewPetStatus(@PathParam("id") Long id) {
        return petService.viewPetStatus(id);
    }

    @PUT
    @Path("/{id}/feed")
    public PetDTO feedPet(@PathParam("id") Long id) {
        petService.feedPet(id);
        return petService.viewPetStatus(id);
    }

    @PUT
    @Path("/{id}/play")
    public PetDTO playWithPet(@PathParam("id") Long id) {
        petService.playWithPet(id);
        return petService.viewPetStatus(id);
    }

    @DELETE
    @Path("/{id}")
    public Response releasePet(@PathParam("id") Long id) {
        petService.releasePet(id);
        return Response.noContent().build();
    }
}
