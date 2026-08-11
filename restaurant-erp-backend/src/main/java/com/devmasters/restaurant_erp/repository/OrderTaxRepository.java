package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderTax;
import com.devmasters.restaurant_erp.repository.custom.OrderTaxCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderTaxRepository extends MongoRepository<OrderTax, UUID>, OrderTaxCustomRepository {

    List<OrderTax> findByOrder_IdAndOrganization_Id(UUID orderId, UUID organizationId);

    boolean existsByTaxCodeIgnoreCaseAndOrganization_Id(String taxCode, UUID organizationId);
}