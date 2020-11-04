package com.coelho.services;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Customer;
import com.coelho.repositories.CustomerRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

@QuarkusTest
class CustomerServiceTest {

    private static final Integer LIMIT = 10;
    private static final Integer OFFSET = 0;

    @Inject
    CustomerService service;

    @Test
    void testCreate() {
        Customer customer = service.create(Customer.builder().name("test").build());

        Assertions.assertEquals("test", customer.getName());
        Assertions.assertNotNull(customer.getId());
    }

    @Test
    void testUpdate() {
        var customer = service.create(Customer.builder().name("test").build());
        service.update(customer.getId(), Customer.builder().name("test-new").build());

        Assertions.assertEquals("test-new", service.findById(customer.getId()).getName());
    }

    @Test
    void testUpdateNotFound() {
        var id = UUID.randomUUID();
        var customer = Customer.builder()
                .id(UUID.randomUUID())
                .name("test")
                .build();

        Assertions.assertThrows(
                NotFoundException.class, () -> service.update(id, customer)
        );
    }

    @Test
    void testDelete() {
        var customer = service.create(Customer.builder().name("test").build());
        service.delete(customer.getId());

        Assertions.assertThrows(
                NotFoundException.class, () -> service.findById(customer.getId())
        );
    }
}
