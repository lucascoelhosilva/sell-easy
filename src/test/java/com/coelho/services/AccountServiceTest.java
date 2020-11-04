package com.coelho.services;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Account;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

@QuarkusTest
class AccountServiceTest extends BaseServiceTest {

    @Inject
    AccountService service;

    @Test
    void testCreate() {
        Account account = service.create(Account.builder().name("test").customer(aCustomer()).build());

        Assertions.assertEquals("test", account.getName());
        Assertions.assertNotNull(account.getId());
    }

    @Test
    void testUpdateName() {
        var account = service.create(Account.builder().name("test").customer(aCustomer()).build());
        service.update(account.getId(), Account.builder().name("test-new").build());

        Assertions.assertEquals("test-new", service.findById(account.getId()).getName());
    }

    @Test
    void testUpdateNotFound() {
        var id = UUID.randomUUID();
        var account = Account.builder()
                .id(UUID.randomUUID())
                .name("test")
                .build();

        Assertions.assertThrows(
                NotFoundException.class, () -> service.update(id, account)
        );
    }

    @Test
    void testDelete() {
        var account = service.create(Account.builder().name("test").customer(aCustomer()).build());
        service.delete(account.getId());

        Assertions.assertThrows(
                NotFoundException.class, () -> service.findById(account.getId())
        );
    }
}
