package com.devmasters.restaurant_erp.expense.respository;

import com.devmasters.restaurant_erp.expense.domain.ExpenseApproval;
import com.devmasters.restaurant_erp.expense.respository.custom.ExpenseApprovalCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseApprovalRepository extends MongoRepository<ExpenseApproval, UUID>,
        ExpenseApprovalCustomRepository {

}