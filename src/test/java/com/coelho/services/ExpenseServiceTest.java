package com.coelho.services;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Expense;
import io.quarkus.test.junit.QuarkusTest;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@QuarkusTest
class ExpenseServiceTest extends BaseServiceTest {

    @Inject
    ExpenseService service;

    @Test
    void testFindAll() {
        Collection<Expense> expenses = service.findAll(OFFSET, LIMIT);
        Assertions.assertTrue(expenses.isEmpty());
    }

    @Test
    void testCreate() {
        Expense expense = service.create(aExpense());

        Assertions.assertEquals("test", expense.getDescription());
        Assertions.assertNotNull(expense.getId());
    }

    @Test
    void testUpdate() {
        var expense = service.create(aExpense());

        service.update(expense.getId(), Expense.builder().description("test-new").build());

        Assertions.assertEquals("test-new", service.findById(expense.getId()).getDescription());
    }

    @Test
    void testUpdateNotFound() {
        var id = UUID.randomUUID();
        var expense = Expense.builder()
                .id(UUID.randomUUID())
                .description("test")
                .build();

        Assertions.assertThrows(
                NotFoundException.class, () -> service.update(id, expense)
        );
    }

    @Test
    void testDelete() {
        var category = service.create(aExpense());
        service.delete(category.getId());

        Assertions.assertThrows(
                NotFoundException.class, () -> service.findById(category.getId())
        );
    }

    private Expense aExpense() {
        return Expense.builder()
                .description("test")
                .customer(aCustomer())
                .category(aCategoryExpense())
                .account(aAccount())
                .date(LocalDate.now())
                .paid(true)
                .recurrent(true)
                .reminder(true)
                .value(Money.of(10, "BRL"))
                .build();
    }
}
