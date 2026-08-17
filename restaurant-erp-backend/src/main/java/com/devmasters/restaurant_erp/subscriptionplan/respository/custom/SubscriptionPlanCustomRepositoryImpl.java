package com.devmasters.restaurant_erp.subscriptionplan.respository.custom;

import com.devmasters.restaurant_erp.subscriptionplan.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.subscriptionplan.model.searchCriteria.SubscriptionPlanSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubscriptionPlanCustomRepositoryImpl implements SubscriptionPlanCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<SubscriptionPlan> search(SubscriptionPlanSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getName() != null && !criteria.getName().isBlank())
            filters.add(Criteria.where("name").regex(criteria.getName(), "i"));

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));

        long total = mongoTemplate.count(query, SubscriptionPlan.class);

        query.with(pageable);

        List<SubscriptionPlan> plans = mongoTemplate.find(query, SubscriptionPlan.class);

        return new PageImpl<>(plans, pageable, total);
    }
}
