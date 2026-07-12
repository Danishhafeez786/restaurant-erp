package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Expense.Expense;
import com.devmasters.restaurant_erp.repository.custom.ExpenseCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, UUID>, ExpenseCustomRepository {

    boolean existsByExpenseNoAndOrganization_Id(
            String expenseNo,
            UUID organizationId);
}