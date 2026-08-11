package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderDelivery;
import com.devmasters.restaurant_erp.repository.custom.OrderDeliveryCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderDeliveryRepository extends MongoRepository<OrderDelivery, UUID>, OrderDeliveryCustomRepository {

    Optional<OrderDelivery> findByOrder_IdAndOrganization_Id(UUID orderId, UUID organizationId);

    boolean existsByOrder_IdAndOrganization_Id(UUID orderId, UUID organizationId);
}