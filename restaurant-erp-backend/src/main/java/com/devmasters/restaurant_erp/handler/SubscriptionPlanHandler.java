package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.model.SubscriptionModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.SubscriptionPlanSearchCriteria;
import com.devmasters.restaurant_erp.service.SubscriptionPlanService;
import com.devmasters.restaurant_erp.transformer.SubscriptionPlanTransformer;
import com.devmasters.restaurant_erp.websocket.SubscriptionPlanEventPublisher;
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
    private final SubscriptionPlanEventPublisher eventPublisher;

    public SubscriptionModel create(SubscriptionModel model) {
        if (subscriptionPlanService.existsByNameIgnoreCase(model.getName())) {
            throw new RuntimeException(
                    "Subscription Plan already exists : "
                            + model.getName()
            );
        }
        SubscriptionPlan subscriptionPlan = subscriptionPlanTransformer.toEntity(model);
        SubscriptionModel response =
                subscriptionPlanTransformer.toModel(subscriptionPlanService.create(subscriptionPlan));
        eventPublisher.created(response);

        return response;
    }

    public PageResponse<SubscriptionModel> getAll(SubscriptionPlanSearchCriteria criteria,
            Pageable pageable) {

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

    public SubscriptionModel update(UUID id, SubscriptionModel model) {

        SubscriptionPlan existing = subscriptionPlanService.findById(id);

        if (!existing.getName().equalsIgnoreCase(model.getName())
                && subscriptionPlanService.existsByNameIgnoreCase(model.getName())) {

            throw new RuntimeException("Subscription Plan already exists : " + model.getName());
        }

        SubscriptionPlan entity = subscriptionPlanTransformer.toEntity(model);

        SubscriptionModel response =
                subscriptionPlanTransformer.toModel(subscriptionPlanService.update(id, entity));
        eventPublisher.updated(response);

        return response;
    }

    public void delete(UUID id) {
        SubscriptionPlan deletedPlan = subscriptionPlanService.delete(id);
        eventPublisher.deleted(subscriptionPlanTransformer.toModel(deletedPlan));
    }

    public void restore(UUID id) {
        SubscriptionPlan restored = subscriptionPlanService.restore(id);
        eventPublisher.restored(subscriptionPlanTransformer.toModel(restored));
    }
}
