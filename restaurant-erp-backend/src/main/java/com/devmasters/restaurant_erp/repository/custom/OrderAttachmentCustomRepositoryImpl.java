package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderAttachment;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderAttachmentSearchCriteria;
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
public class OrderAttachmentCustomRepositoryImpl implements OrderAttachmentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderAttachment> search(OrderAttachmentSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria != null) {

            if (StringUtils.hasText(criteria.getSearchInput())) {

                String searchInput = criteria.getSearchInput().trim();

                filters.add(new Criteria().orOperator(Criteria.where("fileName").regex(searchInput, "i"),

                        Criteria.where("contentType").regex(searchInput, "i")));
            }

            if (criteria.getAttachmentType() != null) {

                filters.add(Criteria.where("attachmentType").is(criteria.getAttachmentType()));
            }

            if (criteria.getOrderId() != null) {

                filters.add(Criteria.where("order.$id").is(criteria.getOrderId()));
            }

            if (criteria.getOrganizationId() != null) {

                filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));
            }

            if (criteria.getBranchId() != null) {

                filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));
            }
        }

        if (!filters.isEmpty()) {

            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), OrderAttachment.class);

        query.with(pageable);

        List<OrderAttachment> attachments = mongoTemplate.find(query, OrderAttachment.class);

        return new PageImpl<>(attachments, pageable, total);
    }
}