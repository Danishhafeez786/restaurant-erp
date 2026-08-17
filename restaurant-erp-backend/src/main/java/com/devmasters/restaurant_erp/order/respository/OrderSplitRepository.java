package com.devmasters.restaurant_erp.order.respository;

import com.devmasters.restaurant_erp.order.domain.OrderSplit;
import com.devmasters.restaurant_erp.order.respository.custom.OrderSplitCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderSplitRepository extends MongoRepository<OrderSplit, UUID>, OrderSplitCustomRepository {

    List<OrderSplit> findByOrder_IdAndOrganization_Id(UUID orderId, UUID organizationId);

    boolean existsBySplitNumberIgnoreCaseAndOrganization_Id(String splitNumber, UUID organizationId);
}