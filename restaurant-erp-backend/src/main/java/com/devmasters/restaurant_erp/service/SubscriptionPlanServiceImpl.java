package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.model.searchcriteria.SubscriptionPlanSearchCriteria;
import com.devmasters.restaurant_erp.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService{

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return subscriptionPlanRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public SubscriptionPlan create(SubscriptionPlan entity) {
        subscriptionPlanRepository.save(entity);
        return entity;
    }

    @Override
    public Page<SubscriptionPlan> search(SubscriptionPlanSearchCriteria criteria, Pageable pageable) {
        return subscriptionPlanRepository.search(criteria, pageable
        );
    }
}
