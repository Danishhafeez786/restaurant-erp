package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderDiscount;
import com.devmasters.restaurant_erp.repository.custom.OrderDiscountCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDiscountRepository extends MongoRepository<OrderDiscount, UUID>, OrderDiscountCustomRepository {

    boolean existsByDiscountNumberIgnoreCaseAndOrganization_IdAndIsActiveTrue(String discountNumber, UUID organizationId);

    boolean existsByDiscountNameIgnoreCaseAndOrganization_IdAndIsActiveTrue(String discountName, UUID organizationId);

    boolean existsByDiscountNumberIgnoreCaseAndOrganization_IdAndIsActiveTrueAndIdNot(
            String discountNumber, UUID organizationId, UUID id);

    boolean existsByDiscountNameIgnoreCaseAndOrganization_IdAndIsActiveTrueAndIdNot(
            String discountName, UUID organizationId, UUID id);

    boolean existsByOrderIdAndOrganization_IdAndIsActiveTrue(UUID orderId, UUID organizationId);
}