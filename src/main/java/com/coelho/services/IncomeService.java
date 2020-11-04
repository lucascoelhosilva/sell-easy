package com.coelho.services;

import com.coelho.events.TransactionEvent;
import com.coelho.events.TransactionType;
import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Expense;
import com.coelho.models.Income;
import com.coelho.repositories.IncomeRepository;
import com.coelho.utils.BeanUtils;
import io.quarkus.panache.common.Page;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class IncomeService {

    private static final String NOT_FOUND_MESSAGE = "Income not found";

    private final IncomeRepository repository;
    private final BeanUtils beanUtils;
    private final Event<TransactionEvent> event;

    public IncomeService(IncomeRepository repository, BeanUtils beanUtils, Event<TransactionEvent> event) {
        this.repository = repository;
        this.beanUtils = beanUtils;
        this.event = event;
    }

    public Collection<Income> findAll(Integer offset, Integer limit) {
        return repository.findAll()
                .page(Page.of(offset, limit))
                .list();
    }

    public Income findById(UUID id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    @Transactional
    public Income create(Income income) {
        log.info("Creating income {}", income);
        repository.persistAndFlush(income);

        createTransactionEvent(income, TransactionType.INSERT);
        return income;
    }

    @Transactional
    public void update(UUID id, Income income) {
        log.info("Income updating {}", income);

        Income incomeDB = findById(id);
        income.setId(id);

        try {
            beanUtils.copyProperties(incomeDB, income);
        } catch (Exception e) {
            log.error("Error copying properties of Income entity {}", e.getCause().getMessage());
        }

        repository.persist(incomeDB);
    }

    @Transactional
    public void delete(UUID id) {
        repository.findByIdOptional(id)
                .ifPresent(income -> createTransactionEvent(income, TransactionType.DELETE));
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    private void createTransactionEvent(Income income, TransactionType type) {
        event.fireAsync(TransactionEvent.builder()
                .accountId(income.getAccount().getId())
                .value(income.getValue())
                .type(type)
                .build()
        );
    }
}
