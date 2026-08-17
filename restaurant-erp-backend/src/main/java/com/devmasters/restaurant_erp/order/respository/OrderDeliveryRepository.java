package com.devmasters.restaurant_erp.order.respository;

import com.devmasters.restaurant_erp.order.domain.OrderDelivery;
import com.devmasters.restaurant_erp.order.respository.custom.OrderDeliveryCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDeliveryRepository extends MongoRepository<OrderDelivery, UUID>, OrderDeliveryCustomRepository {
    boolean existsByOrder_Id(UUID orderId);
}