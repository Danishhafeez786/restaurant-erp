package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.repository.custom.SubscriptionPlanCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriptionPlanRepository  extends MongoRepository<SubscriptionPlan, UUID>, SubscriptionPlanCustomRepository {
    boolean existsByNameIgnoreCase(String name);
}
