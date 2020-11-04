package com.coelho.services;

import com.coelho.enums.CategoryEnum;
import com.coelho.models.Account;
import com.coelho.models.Category;
import com.coelho.models.Customer;

import javax.inject.Inject;
import java.util.UUID;

public abstract class BaseServiceTest {

    protected static final Integer LIMIT = 10;
    protected static final Integer OFFSET = 0;

    @Inject
    CustomerService customerService;

    @Inject
    AccountService accountService;

    @Inject
    CategoryService categoryService;

    protected Customer aCustomer() {
        if (customerService.findAll(OFFSET, LIMIT).isEmpty()) {
            return customerService.create(Customer.builder().name(UUID.randomUUID().toString()).build());
        } else {
            return customerService.findAll(OFFSET, LIMIT).stream().findFirst().get();
        }
    }

    protected Account aAccount() {
        if (accountService.findAll(OFFSET, LIMIT).isEmpty()) {
            return accountService.create(Account.builder().name(UUID.randomUUID().toString()).customer(aCustomer()).build());
        } else {
            return accountService.findAll(OFFSET, LIMIT).stream().findFirst().get();
        }
    }

    protected Category aCategoryExpense() {
        if (categoryService.findAll(OFFSET, LIMIT).isEmpty()) {
            return categoryService.create(Category.builder().name(UUID.randomUUID().toString()).type(CategoryEnum.EXPENSE).customer(aCustomer()).build());
        } else {
            return categoryService.findAll(OFFSET, LIMIT).stream().findFirst().get();
        }
    }

    protected Category aCategoryIncome() {
        if (categoryService.findAll(OFFSET, LIMIT).isEmpty()) {
            return categoryService.create(Category.builder().name(UUID.randomUUID().toString()).type(CategoryEnum.INCOME).customer(aCustomer()).build());
        } else {
            return categoryService.findAll(OFFSET, LIMIT).stream().findFirst().get();
        }
    }
}
