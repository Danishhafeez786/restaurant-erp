package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.order.OrderAttachment;
import com.devmasters.restaurant_erp.repository.custom.OrderAttachmentCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderAttachmentRepository extends MongoRepository<OrderAttachment, UUID>, OrderAttachmentCustomRepository {

    List<OrderAttachment> findByOrder_IdAndOrganization_Id(UUID orderId, UUID organizationId);
}
