package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.model.searchcriteria.SubscriptionPlanSearchCriteria;
import com.devmasters.restaurant_erp.repository.SubscriptionPlanRepository;
import com.devmasters.restaurant_erp.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

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

    @Override
    public SubscriptionPlan update(UUID id, SubscriptionPlan entity) {
        SubscriptionPlan existing = findById(id);

        existing.setName(entity.getName());
        existing.setBranchesLimit(entity.getBranchesLimit());
        existing.setUsersLimit(entity.getUsersLimit());
        existing.setMenuItemsLimit(entity.getMenuItemsLimit());
        existing.setOrdersPerMonth(entity.getOrdersPerMonth());
        existing.setMonthlyPrice(entity.getMonthlyPrice());
        existing.setYearlyPrice(entity.getYearlyPrice());
        existing.setIsActive(entity.getIsActive());

        return subscriptionPlanRepository.save(existing);
    }

    @Override
    public SubscriptionPlan findById(UUID id) {
        return subscriptionPlanRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Subscription Plan not found."));
    }

    @Override
    public SubscriptionPlan delete(UUID id) {
        SubscriptionPlan subscriptionPlan = findById(id);

        if (!Boolean.TRUE.equals(subscriptionPlan.getIsActive())) {
            throw new RuntimeException("Subscription Plan already deleted.");}

        subscriptionPlan.setIsActive(false);
        return subscriptionPlanRepository.save(subscriptionPlan);
    }

    @Override
    public SubscriptionPlan restore(UUID id) {

        SubscriptionPlan subscriptionPlan = findById(id);

        subscriptionPlan.setIsActive(true);

        return subscriptionPlanRepository.save(
                subscriptionPlan
        );
    }
}
