package com.coelho.controllers;

import com.coelho.config.Constants;
import com.coelho.dto.CustomerDTO;
import com.coelho.services.CustomerService;
import lombok.val;

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
@Path("/customers")
@RequestScoped
public class CustomerController {

    @Context
    UriInfo uriInfo;

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerDTO> findAll(
            @DefaultValue("0") @QueryParam(value = Constants.OFFSET) Integer offset,
            @DefaultValue("10") @QueryParam(value = Constants.LIMIT) Integer limit
    ) {
        var customers = service.findAll(offset, limit);
        return customers.stream()
                .map(customer -> CustomerDTO.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .build()
                ).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public CustomerDTO findById(@PathParam("id") UUID id) {
        var customer = service.findById(id);
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .build();
    }

    @POST
    public Response create(@Valid CustomerDTO customerDTO) {
        var customer = service.create(customerDTO.toModel());
        return Response.created(buildLocation(customer.getId())).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(
            @PathParam("id") UUID id,
            @Valid CustomerDTO customerDTO
    ) {
        service.update(id, customerDTO.toModel());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") UUID id){
        service.delete(id);
    }

    private URI buildLocation(UUID id) {
        return uriInfo.getAbsolutePathBuilder()
                .path(CustomerController.class, "findById")
                .build(id);
    }
}
