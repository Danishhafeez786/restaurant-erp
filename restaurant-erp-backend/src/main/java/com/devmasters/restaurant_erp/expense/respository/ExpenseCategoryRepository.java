package com.devmasters.restaurant_erp.expense.respository;

import com.devmasters.restaurant_erp.expense.domain.ExpenseCategory;
import com.devmasters.restaurant_erp.expense.respository.custom.ExpenseCategoryCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseCategoryRepository extends MongoRepository<ExpenseCategory, UUID>,
        ExpenseCategoryCustomRepository {

    boolean existsByCategoryCodeIgnoreCaseAndOrganization_Id(String categoryCode, UUID organizationId);

    boolean existsByCategoryNameIgnoreCaseAndOrganization_Id(String categoryName, UUID organizationId);
}
