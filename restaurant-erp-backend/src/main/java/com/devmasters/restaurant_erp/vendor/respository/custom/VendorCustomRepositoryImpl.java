package com.devmasters.restaurant_erp.vendor.respository.custom;

import com.devmasters.restaurant_erp.vendor.domain.Vendor;
import com.devmasters.restaurant_erp.vendor.model.searchCriteria.VendorSearchCriteria;
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
public class VendorCustomRepositoryImpl implements VendorCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Vendor> search(VendorSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getVendorName() != null && !criteria.getVendorName().isBlank()) {
            filters.add(
                    Criteria.where("vendorName")
                            .regex(criteria.getVendorName(), "i"));
        }

        if (criteria.getVendorCode() != null && !criteria.getVendorCode().isBlank()) {
            filters.add(
                    Criteria.where("vendorCode")
                            .regex(criteria.getVendorCode(), "i"));
        }

        if (criteria.getContactPerson() != null && !criteria.getContactPerson().isBlank()) {
            filters.add(
                    Criteria.where("contactPerson")
                            .regex(criteria.getContactPerson(), "i"));
        }

        if (criteria.getPhone() != null && !criteria.getPhone().isBlank()) {
            filters.add(
                    Criteria.where("phone")
                            .regex(criteria.getPhone(), "i"));
        }

        if (criteria.getEmail() != null && !criteria.getEmail().isBlank()) {
            filters.add(
                    Criteria.where("email")
                            .regex(criteria.getEmail(), "i"));
        }

        if (criteria.getCity() != null && !criteria.getCity().isBlank()) {
            filters.add(
                    Criteria.where("city")
                            .regex(criteria.getCity(), "i"));
        }

        if (criteria.getState() != null && !criteria.getState().isBlank()) {
            filters.add(
                    Criteria.where("state")
                            .regex(criteria.getState(), "i"));
        }

        if (criteria.getCountry() != null && !criteria.getCountry().isBlank()) {
            filters.add(
                    Criteria.where("country")
                            .regex(criteria.getCountry(), "i"));
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(
                    Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId()));
        }

        if (criteria.getBranchId() != null) {
            filters.add(
                    Criteria.where("branch.$id")
                            .is(criteria.getBranchId()));
        }

        if (criteria.getIsActive() != null) {
            filters.add(
                    Criteria.where("isActive")
                            .is(criteria.getIsActive()));
        }

        if (!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(
                            filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Vendor.class);
        query.with(pageable);
        List<Vendor> vendors =
                mongoTemplate.find(
                        query,
                        Vendor.class);

        return new PageImpl<>(vendors, pageable, total);
    }
}