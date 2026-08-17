package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderAttachment;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderAttachmentSearchCriteria;
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
public class OrderAttachmentCustomRepositoryImpl
        implements OrderAttachmentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderAttachment> search(OrderAttachmentSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getOrderId() != null) {
            filters.add(
                    Criteria.where("order.$id")
                            .is(criteria.getOrderId())
            );
        }

        if (criteria.getAttachmentType() != null) {
            filters.add(
                    Criteria.where("attachmentType")
                            .is(criteria.getAttachmentType())
            );
        }

        if (criteria.getFileType() != null &&
                !criteria.getFileType().isBlank()) {

            filters.add(
                    Criteria.where("fileType")
                            .is(criteria.getFileType())
            );
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(
                    Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId())
            );
        }

        if (criteria.getBranchId() != null) {
            filters.add(
                    Criteria.where("branch.$id")
                            .is(criteria.getBranchId())
            );
        }

        if (criteria.getIsActive() != null) {
            filters.add(
                    Criteria.where("isActive")
                            .is(criteria.getIsActive())
            );
        }

        if (!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(filters)
            );
        }

        long total =
                mongoTemplate.count(
                        query,
                        OrderAttachment.class
                );

        query.with(pageable);

        return new PageImpl<>(
                mongoTemplate.find(
                        query,
                        OrderAttachment.class
                ),
                pageable,
                total
        );
    }
}