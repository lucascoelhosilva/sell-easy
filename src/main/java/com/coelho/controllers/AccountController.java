package com.coelho.controllers;

import com.coelho.config.Constants;
import com.coelho.dto.AccountDTO;
import com.coelho.services.AccountService;

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
@Path("/accounts")
@RequestScoped
public class AccountController {

    @Context
    UriInfo uriInfo;

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDTO> findAll(
            @DefaultValue("0") @QueryParam(value = Constants.OFFSET) Integer offset,
            @DefaultValue("10") @QueryParam(value = Constants.LIMIT) Integer limit
    ) {
        var accounts = service.findAll(offset, limit);
        return accounts.stream()
                .map(account -> AccountDTO.builder()
                        .id(account.getId())
                        .name(account.getName())
                        .total(account.getTotal())
                        .build()
                ).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public AccountDTO findById(@PathParam("id") UUID id) {
        var account = service.findById(id);
        return AccountDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .total(account.getTotal())
                .build();
    }

    @POST
    public Response create(@Valid AccountDTO accountDTO) {
        var customer = service.create(accountDTO.toModel());
        return Response.created(buildLocation(customer.getId())).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, @Valid AccountDTO accountDTO) {
        service.update(id, accountDTO.toModel());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") UUID id) {
        service.delete(id);
    }

    private URI buildLocation(UUID id) {
        return uriInfo.getAbsolutePathBuilder()
                .path(AccountController.class, "findById")
                .build(id);
    }
}
