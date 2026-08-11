package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderSplit;
import com.devmasters.restaurant_erp.repository.custom.OrderSplitCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderSplitRepository extends MongoRepository<OrderSplit, UUID>, OrderSplitCustomRepository {

    List<OrderSplit> findByOrder_IdAndOrganization_IdOrderBySplitNumberAsc(UUID orderId, UUID organizationId);

    boolean existsByOrder_IdAndSplitNumberAndOrganization_Id(UUID orderId, Integer splitNumber, UUID organizationId);
}