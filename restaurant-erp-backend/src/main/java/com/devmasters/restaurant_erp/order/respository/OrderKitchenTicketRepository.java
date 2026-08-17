package com.devmasters.restaurant_erp.order.respository;

import com.devmasters.restaurant_erp.order.domain.OrderKitchenTicket;
import com.devmasters.restaurant_erp.order.respository.custom.OrderKitchenTicketCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderKitchenTicketRepository extends MongoRepository<OrderKitchenTicket, UUID>, OrderKitchenTicketCustomRepository {

    boolean existsByOrder_Id(UUID orderId);
}