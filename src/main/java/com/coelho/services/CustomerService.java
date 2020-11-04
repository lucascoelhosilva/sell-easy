package com.coelho.services;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Customer;
import com.coelho.repositories.CustomerRepository;
import com.coelho.utils.BeanUtils;
import io.quarkus.panache.common.Page;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class CustomerService {

    private static final String NOT_FOUND_MESSAGE = "Customer not found";

    private final CustomerRepository repository;
    private final BeanUtils beanUtils;

    public CustomerService(CustomerRepository repository, BeanUtils beanUtils) {
        this.repository = repository;
        this.beanUtils = beanUtils;
    }

    public Collection<Customer> findAll(Integer offset, Integer limit) {
        return repository.findAll()
                .page(Page.of(offset, limit))
                .list();
    }

    public Customer findById(UUID id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    @Transactional
    public Customer create(Customer customer) {
        log.info("Creating customer {}", customer);
        repository.persistAndFlush(customer);
        return customer;
    }

    @Transactional
    public void update(UUID id, Customer customer) {
        log.info("Customer updating {}", customer);

        Customer customerDB = findById(id);
        customer.setId(id);

        try {
            beanUtils.copyProperties(customerDB, customer);
        } catch (Exception e) {
            log.error("Error copying properties of Customer entity {}", e.getCause().getMessage());
        }

        repository.persist(customerDB);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }
}
