package com.devmasters.restaurant_erp.subscriptionplan.respository.custom;

import com.devmasters.restaurant_erp.subscriptionplan.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.subscriptionplan.model.searchCriteria.SubscriptionPlanSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionPlanCustomRepository {

    Page<SubscriptionPlan> search(SubscriptionPlanSearchCriteria criteria, Pageable pageable);
}
