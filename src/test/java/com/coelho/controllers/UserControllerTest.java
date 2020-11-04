package com.coelho.controllers;

import com.coelho.dto.UserDTO;
import com.coelho.models.Customer;
import com.coelho.models.User;
import com.coelho.services.UserService;
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
class UserControllerTest {

    private static final String RESOURCE = "/users";

    @InjectMock
    UserService service;

    User user;
    UserDTO userDTO;

    @BeforeEach
    void setup() {
        user = User.builder().build();
        userDTO = UserDTO.builder()
                .name("lucas")
                .email("lucas.coelho@gmail.com")
                .password(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void testFindAll() {
        Mockito.when(service.findAll(Mockito.any(UUID.class), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(user));

        given().when()
                .get(RESOURCE)
                .then()
                .statusCode(200);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();

        Mockito.when(service.findById(id)).thenReturn(user);

        given()
                .when().get(RESOURCE.concat("/").concat(id.toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testCreate() {
        user.setId(UUID.randomUUID());
        Mockito.when(service.create(Mockito.any(User.class))).thenReturn(user);

        var url = "http://localhost:8081/users/".concat(user.getId().toString());

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("customerId", UUID.randomUUID())
                .body(userDTO)
                .post(RESOURCE)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .header("location", Matchers.is(url));
    }

    @Test
    void testUpdate() {
        Mockito.when(service.create(Mockito.any(User.class))).thenReturn(user);
        user.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(userDTO)
                .put(RESOURCE.concat("/").concat(user.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testDelete() {
        Mockito.when(service.create(Mockito.any(User.class))).thenReturn(user);
        user.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .delete(RESOURCE.concat("/").concat(user.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
