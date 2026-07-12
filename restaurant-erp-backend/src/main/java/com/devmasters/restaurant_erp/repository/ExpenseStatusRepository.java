package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseStatus;
import com.devmasters.restaurant_erp.repository.custom.ExpenseStatusCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseStatusRepository extends MongoRepository<ExpenseStatus, UUID>, ExpenseStatusCustomRepository {

    boolean existsByCodeIgnoreCaseAndOrganization_Id(String code, UUID organizationId);

    boolean existsByStatusNameIgnoreCaseAndOrganization_Id(String statusName, UUID organizationId);

    boolean existsByDefaultStatusTrueAndOrganization_Id(UUID organizationId);

    boolean existsByDefaultStatusTrueAndOrganization_IdAndIdNot(UUID organizationId, UUID id);
}
