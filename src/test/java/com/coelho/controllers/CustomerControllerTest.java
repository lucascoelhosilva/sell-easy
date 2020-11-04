package com.coelho.controllers;

import com.coelho.dto.CustomerDTO;
import com.coelho.models.Customer;
import com.coelho.services.CustomerService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;

@QuarkusTest
class CustomerControllerTest {

    private static final String RESOURCE = "/customers";

    @InjectMock
    CustomerService service;

    Customer customer;
    CustomerDTO customerDTO;

    @BeforeEach
    void setup() {
        customer = Customer.builder().build();
        customerDTO = CustomerDTO.builder()
                .name(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void testFindAll() {
        Mockito.when(service.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(customer));

        given().when()
                .get(RESOURCE)
                .then()
                .statusCode(200);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();

        Mockito.when(service.findById(id)).thenReturn(customer);

        given()
                .when().get(RESOURCE.concat("/").concat(id.toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testCreate() {
        customer.setId(UUID.randomUUID());
        Mockito.when(service.create(Mockito.any(Customer.class))).thenReturn(customer);

        var url = "http://localhost:8081/customers/".concat(customer.getId().toString());

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerDTO)
                .post(RESOURCE)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .header("location", Matchers.is(url));
    }

    @Test
    void testUpdate() {
        Mockito.when(service.create(Mockito.any(Customer.class))).thenReturn(customer);
        customer.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(customerDTO)
                .put(RESOURCE.concat("/").concat(customer.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testDelete() {
        Mockito.when(service.create(Mockito.any(Customer.class))).thenReturn(customer);
        customer.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .delete(RESOURCE.concat("/").concat(customer.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

}
