package com.coelho.repositories;

import com.coelho.models.Expense;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class ExpenseRepository implements PanacheRepositoryBase<Expense, UUID> {

}
