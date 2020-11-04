package com.coelho.services;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Category;
import com.coelho.repositories.CategoryRepository;
import com.coelho.utils.BeanUtils;
import io.quarkus.panache.common.Page;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class CategoryService {

    private static final String NOT_FOUND_MESSAGE = "Category not found";

    private final CategoryRepository repository;
    private final BeanUtils beanUtils;

    public CategoryService(CategoryRepository repository, BeanUtils beanUtils) {
        this.repository = repository;
        this.beanUtils = beanUtils;
    }

    public Collection<Category> findAll(Integer offset, Integer limit) {
        return repository.findAll()
                .page(Page.of(offset, limit))
                .list();
    }

    public Category findById(UUID id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    @Transactional
    public Category create(Category category) {
        log.info("Creating category {}", category);
        repository.persist(category);
        return category;
    }

    @Transactional
    public void update(UUID id, Category category) {
        log.info("Category updating {}", category);

        Category categoryDB = findById(id);
        category.setId(id);

        try {
            beanUtils.copyProperties(categoryDB, category);
        } catch (Exception e) {
            log.error("Error copying properties of Category entity {}", e.getCause().getMessage());
        }

        repository.persist(categoryDB);
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
