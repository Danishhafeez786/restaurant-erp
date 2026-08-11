package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderKitchenTicket;
import com.devmasters.restaurant_erp.repository.custom.OrderKitchenTicketCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderKitchenTicketRepository extends MongoRepository<OrderKitchenTicket, UUID>, OrderKitchenTicketCustomRepository {

    List<OrderKitchenTicket> findByOrder_IdAndOrganization_Id(UUID orderId, UUID organizationId);

    boolean existsByTicketNumberIgnoreCaseAndOrganization_Id(String ticketNumber, UUID organizationId);
}
