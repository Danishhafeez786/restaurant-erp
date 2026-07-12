package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseAttachment;
import com.devmasters.restaurant_erp.repository.custom.ExpenseAttachmentCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseAttachmentRepository extends MongoRepository<ExpenseAttachment, UUID>,
        ExpenseAttachmentCustomRepository {

}