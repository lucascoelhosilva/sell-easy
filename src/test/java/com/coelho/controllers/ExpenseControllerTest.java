package com.coelho.controllers;

import com.coelho.dto.ExpenseDTO;
import com.coelho.models.Account;
import com.coelho.models.Category;
import com.coelho.models.Customer;
import com.coelho.models.Expense;
import com.coelho.services.ExpenseService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;

@QuarkusTest
class ExpenseControllerTest {

    private static final String RESOURCE = "/expenses";

    @InjectMock
    ExpenseService service;

    Expense expense;
    ExpenseDTO expenseDTO;

    @BeforeEach
    void setup() {
        expense = Expense.builder()
                .description(UUID.randomUUID().toString())
                .customer(Customer.builder().id(UUID.randomUUID()).build())
                .account(Account.builder().id(UUID.randomUUID()).build())
                .category(Category.builder().id(UUID.randomUUID()).build())
                .build();
        expenseDTO = ExpenseDTO.builder()
                .description(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .accountId(UUID.randomUUID())
                .build();
    }

    @Test
    void testFindAll() {
        Mockito.when(service.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(expense));

        given().when()
                .get(RESOURCE)
                .then()
                .statusCode(200);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();

        Mockito.when(service.findById(id)).thenReturn(expense);

        given()
                .when().get(RESOURCE.concat("/").concat(id.toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testCreate() {
        expense.setId(UUID.randomUUID());
        Mockito.when(service.create(Mockito.any(Expense.class))).thenReturn(expense);

        var url = "http://localhost:8081/expenses/".concat(expense.getId().toString());

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(expenseDTO)
                .post(RESOURCE)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .header("location", Matchers.is(url));
    }

    @Test
    void testUpdate() {
        Mockito.when(service.create(Mockito.any(Expense.class))).thenReturn(expense);
        expense.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(expenseDTO)
                .put(RESOURCE.concat("/").concat(expense.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testDelete() {
        Mockito.when(service.create(Mockito.any(Expense.class))).thenReturn(expense);
        expense.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .delete(RESOURCE.concat("/").concat(expense.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
