package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.PaymentMethod;
import com.devmasters.restaurant_erp.repository.custom.PaymentMethodCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentMethodRepository extends MongoRepository<PaymentMethod, UUID>, PaymentMethodCustomRepository {

    boolean existsByCodeIgnoreCaseAndOrganization_Id(String code, UUID organizationId);

    boolean existsByMethodNameIgnoreCaseAndOrganization_Id(String methodName, UUID organizationId);
}