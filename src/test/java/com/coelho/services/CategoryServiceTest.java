package com.coelho.services;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Category;
import com.coelho.models.Customer;
import com.coelho.repositories.CategoryRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

@QuarkusTest
class CategoryServiceTest extends BaseServiceTest {

    @Inject
    CategoryService service;

    @Test
    void testFindAll() {
        Collection<Category> categories = service.findAll(OFFSET, LIMIT);
        Assertions.assertFalse(categories.isEmpty());
    }

    @Test
    void testCreate() {
        Category category = service.create(Category.builder().name("test").customer(aCustomer()).build());

        Assertions.assertEquals("test", category.getName());
        Assertions.assertNotNull(category.getId());
    }

    @Test
    void testUpdate() {
        var category = service.create(Category.builder().name("test").customer(aCustomer()).build());
        service.update(category.getId(), Category.builder().name("test-new").build());

        Assertions.assertEquals("test-new", service.findById(category.getId()).getName());
    }

    @Test
    void testUpdateNotFound() {
        var id = UUID.randomUUID();
        var category = Category.builder()
                .id(UUID.randomUUID())
                .name("test")
                .build();

        Assertions.assertThrows(
                NotFoundException.class, () -> service.update(id, category)
        );
    }

    @Test
    void testDelete() {
        var category = service.create(Category.builder().name("test").customer(aCustomer()).build());
        service.delete(category.getId());

        Assertions.assertThrows(
                NotFoundException.class, () -> service.findById(category.getId())
        );
    }

}
