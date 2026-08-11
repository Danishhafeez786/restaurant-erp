package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderDelivery;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDeliverySearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderDeliveryCustomRepositoryImpl implements OrderDeliveryCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderDelivery> search(OrderDeliverySearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria != null) {

            if (StringUtils.hasText(criteria.getSearchInput())) {

                String searchInput = criteria.getSearchInput().trim();

                filters.add(new Criteria().orOperator(Criteria.where("deliveryAddress.receiverName").regex(searchInput, "i"),

                        Criteria.where("deliveryAddress.phoneNumber").regex(searchInput, "i"),

                        Criteria.where("deliveryAddress.city").regex(searchInput, "i")));
            }

            if (criteria.getStatus() != null) {

                filters.add(Criteria.where("status").is(criteria.getStatus()));
            }

            if (criteria.getOrderId() != null) {

                filters.add(Criteria.where("order.$id").is(criteria.getOrderId()));
            }

            if (criteria.getDeliveryPartnerId() != null) {

                filters.add(Criteria.where("deliveryPartner.$id").is(criteria.getDeliveryPartnerId()));
            }

            if (criteria.getOrganizationId() != null) {

                filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));
            }

            if (criteria.getBranchId() != null) {

                filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));
            }

            if (criteria.getAssignedAtFrom() != null) {

                filters.add(Criteria.where("assignedAt").gte(criteria.getAssignedAtFrom()));
            }

            if (criteria.getAssignedAtTo() != null) {

                filters.add(Criteria.where("assignedAt").lte(criteria.getAssignedAtTo()));
            }
        }

        if (!filters.isEmpty()) {

            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), OrderDelivery.class);

        query.with(pageable);

        List<OrderDelivery> deliveries = mongoTemplate.find(query, OrderDelivery.class);

        return new PageImpl<>(deliveries, pageable, total);
    }
}