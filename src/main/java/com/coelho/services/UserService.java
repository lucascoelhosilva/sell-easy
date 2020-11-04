package com.coelho.services;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.User;
import com.coelho.repositories.UserRepository;
import com.coelho.utils.BeanUtils;
import io.quarkus.panache.common.Page;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class UserService {

    private static final String NOT_FOUND_MESSAGE = "User not found";

    private final UserRepository repository;
    private final BeanUtils beanUtils;

    public UserService(UserRepository repository, BeanUtils beanUtils) {
        this.repository = repository;
        this.beanUtils = beanUtils;
    }

    public Collection<User> findAll(UUID customerId, Integer offset, Integer limit) {
        return repository.find("customer.id", customerId)
                .page(Page.of(offset, limit))
                .list();
    }

    public User findById(UUID id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    @Transactional
    public User create(User user) {
        log.info("Creating user {}", user);
        repository.persistAndFlush(user);
        return user;
    }

    @Transactional
    public void update(UUID id, User user) {
        log.info("User updating {}", user);

        User userDB = findById(id);
        user.setId(id);

        try {
            beanUtils.copyProperties(userDB, user);
        } catch (Exception e) {
            log.error("Error copying properties of User entity {}", e.getCause().getMessage());
        }

        repository.persist(userDB);
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
