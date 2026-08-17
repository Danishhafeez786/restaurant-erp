package com.devmasters.restaurant_erp.expense.respository;

import com.devmasters.restaurant_erp.expense.domain.ExpenseAttachment;
import com.devmasters.restaurant_erp.expense.respository.custom.ExpenseAttachmentCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseAttachmentRepository extends MongoRepository<ExpenseAttachment, UUID>,
        ExpenseAttachmentCustomRepository {

}