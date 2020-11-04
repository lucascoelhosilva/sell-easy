package com.coelho.controllers;

import com.coelho.dto.AccountDTO;
import com.coelho.models.Account;
import com.coelho.services.AccountService;
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
class AccountControllerTest {

    private static final String RESOURCE = "/accounts";

    @InjectMock
    AccountService service;

    Account account;
    AccountDTO accountDTO;

    @BeforeEach
    void setup() {
        account = Account.builder().build();
        accountDTO = AccountDTO.builder()
                .name(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void testFindAll() {
        Mockito.when(service.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(account));

        given().when()
                .get(RESOURCE)
                .then()
                .statusCode(200);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();

        Mockito.when(service.findById(id)).thenReturn(account);

        given()
                .when().get(RESOURCE.concat("/").concat(id.toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testCreate() {
        account.setId(UUID.randomUUID());
        Mockito.when(service.create(Mockito.any(Account.class))).thenReturn(account);

        var url = "http://localhost:8081/accounts/".concat(account.getId().toString());

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountDTO)
                .post(RESOURCE)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .header("location", Matchers.is(url));
    }

    @Test
    void testUpdate() {
        Mockito.when(service.create(Mockito.any(Account.class))).thenReturn(account);
        account.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(accountDTO)
                .put(RESOURCE.concat("/").concat(account.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testDelete() {
        Mockito.when(service.create(Mockito.any(Account.class))).thenReturn(account);
        account.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .delete(RESOURCE.concat("/").concat(account.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
