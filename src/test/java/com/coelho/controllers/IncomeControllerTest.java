package com.coelho.controllers;

import com.coelho.dto.IncomeDTO;
import com.coelho.models.Account;
import com.coelho.models.Category;
import com.coelho.models.Customer;
import com.coelho.models.Income;
import com.coelho.services.IncomeService;
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
class IncomeControllerTest {

    private static final String RESOURCE = "/incomes";

    @InjectMock
    IncomeService service;

    Income income;
    IncomeDTO incomeDTO;

    @BeforeEach
    void setup() {
        income = Income.builder()
                .description(UUID.randomUUID().toString())
                .customer(Customer.builder().id(UUID.randomUUID()).build())
                .account(Account.builder().id(UUID.randomUUID()).build())
                .category(Category.builder().id(UUID.randomUUID()).build())
                .build();
        incomeDTO = IncomeDTO.builder()
                .description(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .accountId(UUID.randomUUID())
                .build();
    }

    @Test
    void testFindAll() {
        Mockito.when(service.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(income));

        given().when()
                .get(RESOURCE)
                .then()
                .statusCode(200);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();

        Mockito.when(service.findById(id)).thenReturn(income);

        given()
                .when().get(RESOURCE.concat("/").concat(id.toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testCreate() {
        income.setId(UUID.randomUUID());
        Mockito.when(service.create(Mockito.any(Income.class))).thenReturn(income);

        var url = "http://localhost:8081/incomes/".concat(income.getId().toString());

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(incomeDTO)
                .post(RESOURCE)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .header("location", Matchers.is(url));
    }

    @Test
    void testUpdate() {
        Mockito.when(service.create(Mockito.any(Income.class))).thenReturn(income);
        income.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(incomeDTO)
                .put(RESOURCE.concat("/").concat(income.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testDelete() {
        Mockito.when(service.create(Mockito.any(Income.class))).thenReturn(income);
        income.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .delete(RESOURCE.concat("/").concat(income.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
