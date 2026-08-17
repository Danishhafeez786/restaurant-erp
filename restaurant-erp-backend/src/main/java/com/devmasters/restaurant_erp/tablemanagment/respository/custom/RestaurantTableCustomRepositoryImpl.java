package com.devmasters.restaurant_erp.tablemanagment.respository.custom;

import com.devmasters.restaurant_erp.tablemanagment.domain.RestaurantTable;
import com.devmasters.restaurant_erp.tablemanagment.model.searchCriteria.RestaurantTableSearchCriteria;
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
public class RestaurantTableCustomRepositoryImpl implements RestaurantTableCustomRepository {

    private final MongoTemplate mongoTemplate;
    @Override
    public Page<RestaurantTable> search(RestaurantTableSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getTableNumber() != null && !criteria.getTableNumber().isBlank()) {
            filters.add(Criteria.where("tableNumber").regex(criteria.getTableNumber(), "i"));
        }

        if (criteria.getTableName() != null && !criteria.getTableName().isBlank()) {
            filters.add(Criteria.where("tableName").regex(criteria.getTableName(), "i"));
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));
        }

        if (criteria.getBranchId() != null) {
            filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));
        }

        if (criteria.getFloorId() != null) {
            filters.add(Criteria.where("floor.$id").is(criteria.getFloorId()));
        }

        if (criteria.getCurrentCustomerId() != null) {
            filters.add(Criteria.where("currentCustomer.$id").is(criteria.getCurrentCustomerId()));
        }

        if (criteria.getAssignedWaiterId() != null) {
            filters.add(Criteria.where("assignedWaiter.$id").is(criteria.getAssignedWaiterId()));
        }

        if (criteria.getStatus() != null) {
            filters.add(Criteria.where("status").is(criteria.getStatus()));
        }

        if (criteria.getCapacity() != null) {
            filters.add(Criteria.where("capacity").is(criteria.getCapacity()));
        }

        if (criteria.getReservable() != null) {
            filters.add(Criteria.where("reservable").is(criteria.getReservable()));
        }

        if (criteria.getMerged() != null) {
            filters.add(Criteria.where("merged").is(criteria.getMerged()));
        }

        if (criteria.getIsActive() != null) {
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));
        }

        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, RestaurantTable.class);

        query.with(pageable);
        List<RestaurantTable> tables = mongoTemplate.find(query, RestaurantTable.class);
        return new PageImpl<>(tables, pageable, total);
    }
}