package com.coelho.controllers;

import com.coelho.config.Constants;
import com.coelho.dto.UserDTO;
import com.coelho.services.UserService;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
@Path("/users")
@RequestScoped
public class UserController {

    @Context
    UriInfo uriInfo;

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDTO> findAll(
            @DefaultValue("0") @QueryParam(value = Constants.OFFSET) Integer offset,
            @DefaultValue("10") @QueryParam(value = Constants.LIMIT) Integer limit,
            @HeaderParam(value = "customerId") UUID customerId
    ) {
        var users = service.findAll(customerId, offset, limit);
        return users.stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build()
                ).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public UserDTO findById(@PathParam("id") UUID id) {
        var user = service.findById(id);
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @POST
    public Response create(@Valid UserDTO userDTO, @HeaderParam("customerId") UUID customerId) {
        userDTO.setCustomerId(customerId);
        var user = service.create(userDTO.toModel());
        return Response.created(buildLocation(user.getId())).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, @Valid UserDTO userDTO) {
        service.update(id, userDTO.toModel());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") UUID id) {
        service.delete(id);
    }

    private URI buildLocation(UUID id) {
        return uriInfo.getAbsolutePathBuilder()
                .path(UserController.class, "findById")
                .build(id);
    }
}
