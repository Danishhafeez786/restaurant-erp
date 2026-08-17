package com.devmasters.restaurant_erp.subscriptionplan.handler;

import com.devmasters.restaurant_erp.subscriptionplan.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.subscriptionplan.model.SubscriptionModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.subscriptionplan.model.searchCriteria.SubscriptionPlanSearchCriteria;
import com.devmasters.restaurant_erp.subscriptionplan.service.SubscriptionPlanService;
import com.devmasters.restaurant_erp.subscriptionplan.transformer.SubscriptionPlanTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class SubscriptionPlanHandler {

    private final SubscriptionPlanService subscriptionPlanService;
    private final SubscriptionPlanTransformer subscriptionPlanTransformer;

    public SubscriptionModel create(SubscriptionModel model) {

        if (subscriptionPlanService.existsByNameIgnoreCase(model.getName())) {
            throw new RuntimeException(
                    "Subscription Plan already exists : " + model.getName()
            );
        }

        SubscriptionPlan entity = subscriptionPlanTransformer.toEntity(model);

        SubscriptionPlan saved = subscriptionPlanService.create(entity);

        return subscriptionPlanTransformer.toModel(saved);
    }

    public PageResponse<SubscriptionModel> getAll(SubscriptionPlanSearchCriteria criteria, Pageable pageable) {

        Page<SubscriptionPlan> page = subscriptionPlanService.search(criteria, pageable);

        return PageResponse.<SubscriptionModel>builder()
                .content(subscriptionPlanTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public SubscriptionModel update(
            UUID id,
            SubscriptionModel model) {

        SubscriptionPlan existing =
                subscriptionPlanService.findById(id);

        if (!existing.getName().equalsIgnoreCase(model.getName())
                && subscriptionPlanService.existsByNameIgnoreCase(model.getName())) {

            throw new RuntimeException(
                    "Subscription Plan already exists : " + model.getName()
            );
        }

        SubscriptionPlan entity =
                subscriptionPlanTransformer.toEntity(model);

        SubscriptionPlan updated =
                subscriptionPlanService.update(id, entity);

        return subscriptionPlanTransformer.toModel(updated);
    }

    public void delete(UUID id) {
        subscriptionPlanService.delete(id);
    }

    public void restore(UUID id) {
        subscriptionPlanService.restore(id);
    }
}