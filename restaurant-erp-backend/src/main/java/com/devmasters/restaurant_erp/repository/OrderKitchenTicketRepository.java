package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderKitchenTicket;
import com.devmasters.restaurant_erp.repository.custom.OrderKitchenTicketCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderKitchenTicketRepository extends MongoRepository<OrderKitchenTicket, UUID>, OrderKitchenTicketCustomRepository {

    boolean existsByOrder_Id(UUID orderId);
}