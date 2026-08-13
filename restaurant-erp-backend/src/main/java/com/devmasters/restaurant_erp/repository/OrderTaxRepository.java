package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderTax;
import com.devmasters.restaurant_erp.repository.custom.OrderTaxCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderTaxRepository extends MongoRepository<OrderTax, UUID>, OrderTaxCustomRepository {

    boolean existsByTaxNumberIgnoreCase(String taxNumber);

    boolean existsByOrder_IdAndTax_Id(UUID orderId, UUID taxId);

    boolean existsByOrder_IdAndTax_IdAndIdNot(UUID orderId, UUID taxId, UUID id);
}