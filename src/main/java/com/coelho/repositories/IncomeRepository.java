package com.coelho.repositories;

import com.coelho.models.Income;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class IncomeRepository implements PanacheRepositoryBase<Income, UUID> {

}
