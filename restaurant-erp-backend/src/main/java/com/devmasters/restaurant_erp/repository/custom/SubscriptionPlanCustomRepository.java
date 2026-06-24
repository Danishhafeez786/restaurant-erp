package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.model.searchcriteria.SubscriptionPlanSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionPlanCustomRepository {

    Page<SubscriptionPlan> search(SubscriptionPlanSearchCriteria criteria, Pageable pageable);
}
