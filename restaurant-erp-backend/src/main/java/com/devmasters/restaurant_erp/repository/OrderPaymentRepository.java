package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderPayment;
import com.devmasters.restaurant_erp.repository.custom.OrderPaymentCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderPaymentRepository extends MongoRepository<OrderPayment, UUID>, OrderPaymentCustomRepository {

    boolean existsByPaymentNumberIgnoreCaseAndOrganization_Id(String paymentNumber, UUID organizationId);

    boolean existsByTransactionReferenceIgnoreCaseAndOrganization_Id(String transactionReference, UUID organizationId);

    boolean existsByPaymentNumberIgnoreCaseAndOrganization_IdAndIdNot(String paymentNumber, UUID organizationId, UUID id);

    boolean existsByTransactionReferenceIgnoreCaseAndOrganization_IdAndIdNot(String transactionReference, UUID organizationId, UUID id);

    boolean existsByOrderIdAndOrganization_Id(UUID orderId, UUID organizationId);
}