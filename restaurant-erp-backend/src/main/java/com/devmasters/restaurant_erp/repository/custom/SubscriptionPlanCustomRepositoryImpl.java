package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.model.searchcriteria.SubscriptionPlanSearchCriteria;
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
    public Page<SubscriptionPlan> search(
            SubscriptionPlanSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getName() != null && !criteria.getName().isBlank())
            filters.add(Criteria.where("name").regex(criteria.getName(), "i"));

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));

        if (criteria.getMinMonthlyPrice() != null)
            filters.add(Criteria.where("monthlyPrice").gte(criteria.getMinMonthlyPrice()));

        if (criteria.getMaxMonthlyPrice() != null)
            filters.add(Criteria.where("monthlyPrice").lte(criteria.getMaxMonthlyPrice()));

        if (criteria.getMinUsersLimit() != null)
            filters.add(Criteria.where("usersLimit").gte(criteria.getMinUsersLimit()));

        if (criteria.getMaxUsersLimit() != null)
            filters.add(Criteria.where("usersLimit").lte(criteria.getMaxUsersLimit()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));


        long total = mongoTemplate.count(query, SubscriptionPlan.class);

        query.with(pageable);

        List<SubscriptionPlan> plans = mongoTemplate.find(query, SubscriptionPlan.class);

        return new PageImpl<>(plans, pageable, total);
    }
}
