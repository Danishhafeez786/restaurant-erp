package com.devmasters.restaurant_erp.expense.respository;

import com.devmasters.restaurant_erp.expense.domain.ExpenseRecurring;
import com.devmasters.restaurant_erp.expense.respository.custom.ExpenseRecurringCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRecurringRepository extends MongoRepository<ExpenseRecurring, UUID>,
        ExpenseRecurringCustomRepository {

}