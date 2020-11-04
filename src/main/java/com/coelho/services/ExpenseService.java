package com.coelho.services;

import com.coelho.events.TransactionEvent;
import com.coelho.events.TransactionType;
import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Expense;
import com.coelho.repositories.ExpenseRepository;
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
public class ExpenseService {

    private static final String NOT_FOUND_MESSAGE = "Expense not found";

    private final ExpenseRepository repository;
    private final BeanUtils beanUtils;
    private final Event<TransactionEvent> event;

    public ExpenseService(ExpenseRepository repository, BeanUtils beanUtils, Event<TransactionEvent> event) {
        this.repository = repository;
        this.beanUtils = beanUtils;
        this.event = event;
    }

    public Collection<Expense> findAll(Integer offset, Integer limit) {
        return repository.findAll()
                .page(Page.of(offset, limit))
                .list();
    }

    public Expense findById(UUID id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    @Transactional
    public Expense create(Expense expense) {
        log.info("Creating expense {}", expense);
        repository.persistAndFlush(expense);

        createTransactionEvent(expense, TransactionType.INSERT);
        return expense;
    }

    @Transactional
    public void update(UUID id, Expense expense) {
        log.info("Expense updating {}", expense);

        Expense expenseDB = findById(id);
        expense.setId(id);

        try {
            beanUtils.copyProperties(expenseDB, expense);
        } catch (Exception e) {
            log.error("Error copying properties of Expense entity {}", e.getCause().getMessage());
        }

        repository.persist(expenseDB);
    }

    @Transactional
    public void delete(UUID id) {
        repository.findByIdOptional(id)
                .ifPresent(expense -> createTransactionEvent(expense, TransactionType.DELETE));
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    private void createTransactionEvent(Expense expense, TransactionType type) {
        event.fireAsync(TransactionEvent.builder()
                .accountId(expense.getAccount().getId())
                .value(expense.getValue())
                .type(type)
                .build()
        );
    }
}
