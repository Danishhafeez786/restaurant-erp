package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.Order;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderSearchCriteria;
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
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Order> search(OrderSearchCriteria criteria, Pageable pageable) {
        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();

        if (criteria != null) {

            if (criteria.getSearchInput() != null && !criteria.getSearchInput().isBlank()) {
                String search = criteria.getSearchInput().trim();
                String regex = ".*" + java.util.regex.Pattern.quote(search) + ".*";
                filters.add(new Criteria().orOperator(Criteria.where("orderNumber").regex(regex, "i"),
                        Criteria.where("tableSessionNumber").regex(regex, "i"),
                        Criteria.where("customerNote").regex(regex, "i"),
                        Criteria.where("internalNote").regex(regex, "i")));
            }


            if (criteria.getStatus() != null) {
                filters.add(Criteria.where("status").is(criteria.getStatus()));
            }

            if (criteria.getOrderType() != null) {
                filters.add(Criteria.where("orderType").is(criteria.getOrderType()));
            }

            if (criteria.getOrderSource() != null) {
                filters.add(Criteria.where("orderSource").is(criteria.getOrderSource()));
            }

            if (criteria.getPaymentStatus() != null) {
                filters.add(Criteria.where("paymentStatus").is(criteria.getPaymentStatus()));
            }

            if (criteria.getOrganizationId() != null) {
                filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));
            }

            if (criteria.getBranchId() != null) {
                filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));
            }

            if (criteria.getCustomerId() != null) {
                filters.add(Criteria.where("customer.$id").is(criteria.getCustomerId()));
            }

            if (criteria.getRestaurantTableId() != null) {
                filters.add(Criteria.where("restaurantTable.$id").is(criteria.getRestaurantTableId()));
            }

            if (criteria.getDeliveryPartnerId() != null) {
                filters.add(Criteria.where("deliveryPartnerId").is(criteria.getDeliveryPartnerId()));
            }
        }

        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Order.class);

        query.with(pageable);

        List<Order> orders = mongoTemplate.find(query, Order.class);
        return new PageImpl<>(orders, pageable, total);
    }
}