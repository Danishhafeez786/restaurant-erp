package com.devmasters.restaurant_erp.expense.respository;

import com.devmasters.restaurant_erp.expense.domain.ExpenseType;
import com.devmasters.restaurant_erp.expense.respository.custom.ExpenseTypeCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseTypeRepository extends MongoRepository<ExpenseType, UUID>,
        ExpenseTypeCustomRepository {

    boolean existsByCodeIgnoreCaseAndOrganization_Id(
            String code,
            UUID organizationId);

    boolean existsByTypeNameIgnoreCaseAndOrganization_Id(
            String typeName,
            UUID organizationId);
}