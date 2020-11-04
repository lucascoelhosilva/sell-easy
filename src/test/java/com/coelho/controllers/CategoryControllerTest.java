package com.coelho.controllers;

import com.coelho.dto.CategoryDTO;
import com.coelho.enums.CategoryEnum;
import com.coelho.models.Account;
import com.coelho.models.Category;
import com.coelho.services.CategoryService;
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
class CategoryControllerTest {

    private static final String RESOURCE = "/categories";

    @InjectMock
    CategoryService service;

    Category category;
    CategoryDTO categoryDTO;

    @BeforeEach
    void setup() {
        category = Category.builder().build();
        categoryDTO = CategoryDTO.builder()
                .name(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void testFindAll() {
        Mockito.when(service.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(category));

        given().when()
                .get(RESOURCE)
                .then()
                .statusCode(200);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();

        Mockito.when(service.findById(id)).thenReturn(category);

        given()
                .when().get(RESOURCE.concat("/").concat(id.toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testCreate() {
        category.setId(UUID.randomUUID());
        Mockito.when(service.create(Mockito.any(Category.class))).thenReturn(category);

        var url = "http://localhost:8081/categories/".concat(category.getId().toString());

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryDTO)
                .post(RESOURCE)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .header("location", Matchers.is(url));
    }

    @Test
    void testUpdate() {
        Mockito.when(service.create(Mockito.any(Category.class))).thenReturn(category);
        category.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(categoryDTO)
                .put(RESOURCE.concat("/").concat(category.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testDelete() {
        Mockito.when(service.create(Mockito.any(Category.class))).thenReturn(category);
        category.setId(UUID.randomUUID());

        given()
                .when()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .delete(RESOURCE.concat("/").concat(category.getId().toString()))
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
