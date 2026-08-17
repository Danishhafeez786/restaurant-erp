package com.devmasters.restaurant_erp.order.respository;

import com.devmasters.restaurant_erp.order.domain.OrderTax;
import com.devmasters.restaurant_erp.order.respository.custom.OrderTaxCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderTaxRepository extends MongoRepository<OrderTax, UUID>, OrderTaxCustomRepository {

    boolean existsByTaxNumberIgnoreCase(String taxNumber);

    boolean existsByOrder_IdAndTax_Id(UUID orderId, UUID taxId);

    boolean existsByOrder_IdAndTax_IdAndIdNot(UUID orderId, UUID taxId, UUID id);
}