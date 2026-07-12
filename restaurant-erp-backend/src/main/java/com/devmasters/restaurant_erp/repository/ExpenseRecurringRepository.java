package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseRecurring;
import com.devmasters.restaurant_erp.repository.custom.ExpenseRecurringCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRecurringRepository extends MongoRepository<ExpenseRecurring, UUID>,
        ExpenseRecurringCustomRepository {

}