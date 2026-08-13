package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderRefund;
import com.devmasters.restaurant_erp.repository.custom.OrderRefundCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRefundRepository extends MongoRepository<OrderRefund, UUID>, OrderRefundCustomRepository {

    List<OrderRefund> findByOrder_IdAndOrganization_Id(UUID orderId, UUID organizationId);

    List<OrderRefund> findByOrderPayment_IdAndOrganization_Id(UUID orderPaymentId, UUID organizationId);

    boolean existsByRefundNumberIgnoreCaseAndOrganization_Id(String refundNumber, UUID organizationId);
}