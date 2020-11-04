package com.coelho.services;

import com.coelho.events.TransactionEvent;
import com.coelho.events.TransactionType;
import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Account;
import com.coelho.repositories.AccountRepository;
import com.coelho.utils.BeanUtils;
import io.quarkus.panache.common.Page;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import javax.money.MonetaryAmount;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class AccountService {

    private static final String NOT_FOUND_MESSAGE = "Account not found";

    private final AccountRepository repository;
    private final BeanUtils beanUtils;

    public AccountService(AccountRepository repository, BeanUtils beanUtils) {
        this.repository = repository;
        this.beanUtils = beanUtils;
    }

    public Collection<Account> findAll(Integer offset, Integer limit) {
        return repository.findAll()
                .page(Page.of(offset, limit))
                .list();
    }

    public Account findById(UUID id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    @Transactional
    public Account create(Account account) {
        log.info("Creating account {}", account);
        if (Objects.isNull(account.getTotal())) {
            account.setTotal(Money.of(00.00, "USD"));
        }
        repository.persistAndFlush(account);
        return account;
    }

    @Transactional
    public void update(UUID id, Account account) {
        log.info("Account updating {}", account);

        Account accountDB = findById(id);
        account.setId(id);

        try {
            beanUtils.copyProperties(accountDB, account);
        } catch (Exception e) {
            log.error("Error copying properties of Account entity {}", e.getCause().getMessage());
        }

        repository.persist(accountDB);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void updateAccount(@ObservesAsync TransactionEvent event) {
        if (event.getType().equals(TransactionType.INSERT)) {
            someTotalAccount(event.getAccountId(), event.getValue());
        } else if (event.getType().equals(TransactionType.DELETE)) {
            subtractTotalAccount(event.getAccountId(), event.getValue());
        }
    }

    private void subtractTotalAccount(UUID accountId, MonetaryAmount monetaryAmount) {
        Account account = this.findById(accountId);
        account.setTotal(account.getTotal().subtract(monetaryAmount));
        this.update(accountId, account);
    }

    private void someTotalAccount(UUID accountId, MonetaryAmount monetaryAmount) {
        Account account = this.findById(accountId);
        account.setTotal(account.getTotal().add(monetaryAmount));
        this.update(accountId, account);
    }
}
