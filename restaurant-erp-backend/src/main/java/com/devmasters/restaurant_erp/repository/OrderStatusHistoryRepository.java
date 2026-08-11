package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderStatusHistory;
import com.devmasters.restaurant_erp.repository.custom.OrderStatusHistoryCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderStatusHistoryRepository extends MongoRepository<OrderStatusHistory, UUID>, OrderStatusHistoryCustomRepository {

    List<OrderStatusHistory> findByOrder_IdAndOrganization_IdOrderByChangedAtDesc(UUID orderId, UUID organizationId);
}