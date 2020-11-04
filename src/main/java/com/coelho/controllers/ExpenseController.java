package com.coelho.controllers;

import com.coelho.config.Constants;
import com.coelho.dto.ExpenseDTO;
import com.coelho.services.ExpenseService;

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
@Path("/expenses")
@RequestScoped
public class ExpenseController {

    @Context
    UriInfo uriInfo;

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExpenseDTO> findAll(
            @DefaultValue("0") @QueryParam(value = Constants.OFFSET) Integer offset,
            @DefaultValue("10") @QueryParam(value = Constants.LIMIT) Integer limit
    ) {
        var expenses = service.findAll(offset, limit);
        return expenses.stream()
                .map(expense -> ExpenseDTO.builder()
                        .id(expense.getId())
                        .description(expense.getDescription())
                        .accountId(expense.getAccount().getId())
                        .categoryId(expense.getCategory().getId())
                        .customerId(expense.getCustomer().getId())
                        .date(expense.getDate())
                        .paid(expense.getPaid())
                        .reminder(expense.getReminder())
                        .recurrent(expense.getRecurrent())
                        .value(expense.getValue())
                        .build()
                ).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public ExpenseDTO findById(@PathParam("id") UUID id) {
        var expense = service.findById(id);
        return ExpenseDTO.builder()
                .id(expense.getId())
                .description(expense.getDescription())
                .accountId(expense.getAccount().getId())
                .categoryId(expense.getCategory().getId())
                .customerId(expense.getCustomer().getId())
                .date(expense.getDate())
                .paid(expense.getPaid())
                .reminder(expense.getReminder())
                .recurrent(expense.getRecurrent())
                .value(expense.getValue())
                .build();
    }

    @POST
    public Response create(@Valid ExpenseDTO expenseDTO) {
        var expense = service.create(expenseDTO.toModel());
        return Response.created(buildLocation(expense.getId())).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(
            @PathParam("id") UUID id,
            @Valid ExpenseDTO expenseDTO
    ) {
        service.update(id, expenseDTO.toModel());
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
