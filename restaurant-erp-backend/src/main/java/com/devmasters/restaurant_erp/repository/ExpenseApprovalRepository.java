package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseApproval;
import com.devmasters.restaurant_erp.repository.custom.ExpenseApprovalCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseApprovalRepository extends MongoRepository<ExpenseApproval, UUID>,
        ExpenseApprovalCustomRepository {

}