package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.model.searchcriteria.SubscriptionPlanSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SubscriptionPlanService {
    boolean existsByNameIgnoreCase(String name);

    SubscriptionPlan create(SubscriptionPlan entity);

    Page<SubscriptionPlan> search(SubscriptionPlanSearchCriteria criteria, Pageable pageable);

    SubscriptionPlan update(UUID id, SubscriptionPlan entity);

    SubscriptionPlan findById(UUID id);

    void delete(UUID id);

    void restore(UUID id);
}
