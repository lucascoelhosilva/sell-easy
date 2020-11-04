package com.coelho.controllers;

import com.coelho.config.Constants;
import com.coelho.dto.IncomeDTO;
import com.coelho.services.IncomeService;

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
@Path("/incomes")
@RequestScoped
public class IncomeController {

    @Context
    UriInfo uriInfo;

    private final IncomeService service;

    public IncomeController(IncomeService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<IncomeDTO> findAll(
            @DefaultValue("0") @QueryParam(value = Constants.OFFSET) Integer offset,
            @DefaultValue("10") @QueryParam(value = Constants.LIMIT) Integer limit
    ) {
        var incomes = service.findAll(offset, limit);
        return incomes.stream()
                .map(income -> IncomeDTO.builder()
                        .id(income.getId())
                        .description(income.getDescription())
                        .accountId(income.getAccount().getId())
                        .categoryId(income.getCategory().getId())
                        .customerId(income.getCustomer().getId())
                        .date(income.getDate())
                        .paid(income.getPaid())
                        .reminder(income.getReminder())
                        .recurrent(income.getRecurrent())
                        .value(income.getValue())
                        .build()
                ).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public IncomeDTO findById(@PathParam("id") UUID id) {
        var income = service.findById(id);
        return IncomeDTO.builder()
                .id(income.getId())
                .description(income.getDescription())
                .accountId(income.getAccount().getId())
                .categoryId(income.getCategory().getId())
                .customerId(income.getCustomer().getId())
                .date(income.getDate())
                .paid(income.getPaid())
                .reminder(income.getReminder())
                .recurrent(income.getRecurrent())
                .value(income.getValue())
                .build();
    }

    @POST
    public Response create(@Valid IncomeDTO incomeDTO) {
        var expense = service.create(incomeDTO.toModel());
        return Response.created(buildLocation(expense.getId())).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(
            @PathParam("id") UUID id,
            @Valid IncomeDTO incomeDTO
    ) {
        service.update(id, incomeDTO.toModel());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") UUID id){
        service.delete(id);
    }

    private URI buildLocation(UUID id) {
        return uriInfo.getAbsolutePathBuilder()
                .path(ExpenseController.class, "findById")
                .build(id);
    }
}
