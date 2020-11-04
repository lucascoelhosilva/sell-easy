package com.coelho.services;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Income;
import io.quarkus.test.junit.QuarkusTest;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@QuarkusTest
class IncomeServiceTest extends BaseServiceTest {

    @Inject
    IncomeService service;

    @Test
    void testFindAll() {
        Collection<Income> incomes = service.findAll(OFFSET, LIMIT);
        Assertions.assertTrue(incomes.isEmpty());
    }

    @Test
    void testCreate() {
        Income income = service.create(aIncome());

        Assertions.assertEquals("test", income.getDescription());
        Assertions.assertNotNull(income.getId());
    }

    @Test
    void testUpdate() {
        var income = service.create(aIncome());

        service.update(income.getId(), Income.builder().description("test-new").build());

        Assertions.assertEquals("test-new", service.findById(income.getId()).getDescription());
    }

    @Test
    void testUpdateNotFound() {
        var id = UUID.randomUUID();
        var income = Income.builder()
                .id(UUID.randomUUID())
                .description("test")
                .build();

        Assertions.assertThrows(
                NotFoundException.class, () -> service.update(id, income)
        );
    }

    @Test
    void testDelete() {
        var category = service.create(aIncome());
        service.delete(category.getId());

        Assertions.assertThrows(
                NotFoundException.class, () -> service.findById(category.getId())
        );
    }

    private Income aIncome() {
        return Income.builder()
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
