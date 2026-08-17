package com.devmasters.restaurant_erp.order.respository;

import com.devmasters.restaurant_erp.order.domain.OrderDiscount;
import com.devmasters.restaurant_erp.order.respository.custom.OrderDiscountCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDiscountRepository extends MongoRepository<OrderDiscount, UUID>, OrderDiscountCustomRepository {

    boolean existsByDiscountNumberIgnoreCase(String discountNumber);

    boolean existsByOrder_IdAndDiscountNameIgnoreCase(UUID orderId, String discountName);

    boolean existsByOrder_IdAndDiscountNameIgnoreCaseAndIdNot(UUID orderId, String discountName, UUID id);
}