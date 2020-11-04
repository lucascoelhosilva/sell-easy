package com.coelho.controllers;

import com.coelho.config.Constants;
import com.coelho.dto.CategoryDTO;
import com.coelho.services.CategoryService;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/categories")
@RequestScoped
public class CategoryController {

    @Context
    UriInfo uriInfo;

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CategoryDTO> findAll(
            @DefaultValue("0") @QueryParam(value = Constants.OFFSET) Integer offset,
            @DefaultValue("10") @QueryParam(value = Constants.LIMIT) Integer limit
    ) {
        var categories = service.findAll(offset, limit);
        return categories.stream()
                .map(category -> CategoryDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType())
                        .build()
                ).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public CategoryDTO findById(@PathParam("id") UUID id) {
        var category = service.findById(id);
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .build();
    }

    @POST
    public Response create(@Valid CategoryDTO categoryDTO) {
        var customer = service.create(categoryDTO.toModel());
        return Response.created(buildLocation(customer.getId())).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, @Valid CategoryDTO categoryDTO) {
        service.update(id, categoryDTO.toModel());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") UUID id) {
        service.delete(id);
    }

    private URI buildLocation(UUID id) {
        return uriInfo.getAbsolutePathBuilder()
                .path(CategoryController.class, "findById")
                .build(id);
    }
}
