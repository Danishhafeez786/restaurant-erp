package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.Order;
import com.devmasters.restaurant_erp.repository.custom.OrderCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends MongoRepository<Order, UUID>, OrderCustomRepository {

    boolean existsByOrderNumberIgnoreCaseAndOrganization_Id(String orderNumber, UUID organizationId);

    boolean existsByOrderNumberIgnoreCaseAndOrganization_IdAndIdNot(String orderNumber, UUID organizationId, UUID id);

    Optional<Order> findByOrderNumberIgnoreCaseAndOrganization_Id(String orderNumber, UUID organizationId);
}