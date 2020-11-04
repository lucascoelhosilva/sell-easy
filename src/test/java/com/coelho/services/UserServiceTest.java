package com.coelho.services;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.User;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

@QuarkusTest
class UserServiceTest extends BaseServiceTest {

    @Inject
    UserService service;

    @Test
    void testCreate() {
        User user = service.create(aUser());

        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void testUpdate() {
        var user = service.create(aUser());
        service.update(user.getId(), User.builder().name("test-new").build());

        Assertions.assertEquals("test-new", service.findById(user.getId()).getName());
    }

    @Test
    void testUpdateNotFound() {
        var id = UUID.randomUUID();
        var user = User.builder()
                .name("test")
                .build();

        Assertions.assertThrows(
                NotFoundException.class, () -> service.update(id, user)
        );
    }

    @Test
    void testDelete() {
        var user = service.create(aUser());
        service.delete(user.getId());

        Assertions.assertThrows(
                NotFoundException.class, () -> service.findById(user.getId())
        );
    }

    User aUser() {
        return User.builder()
                .name(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .customer(aCustomer())
                .build();
    }

}
