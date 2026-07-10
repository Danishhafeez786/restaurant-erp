package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Floor;
import com.devmasters.restaurant_erp.model.searchcriteria.FloorSearchCriteria;
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
public class FloorCustomRepositoryImpl
        implements FloorCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Floor> search(
            FloorSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getFloorName() != null
                && !criteria.getFloorName().isBlank()) {

            filters.add(
                    Criteria.where("floorName")
                            .regex(criteria.getFloorName(), "i")
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
                    new Criteria().andOperator(
                            filters.toArray(new Criteria[0])
                    )
            );
        }

        long total = mongoTemplate.count(query, Floor.class);

        query.with(pageable);

        List<Floor> floors =
                mongoTemplate.find(query, Floor.class);

        return new PageImpl<>(
                floors,
                pageable,
                total
        );
    }
}
