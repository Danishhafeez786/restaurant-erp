package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Menu.Category;
import com.devmasters.restaurant_erp.model.searchcriteria.CategorySearchCriteria;
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
public class CategoryCustomRepositoryImpl
        implements CategoryCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Category> search(CategorySearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getSearchInput() != null && !criteria.getSearchInput().isBlank()) {
            String keyword = criteria.getSearchInput();
            filters.add(new Criteria().orOperator(
                            Criteria.where("categoryCode").regex(keyword, "i"),
                            Criteria.where("categoryName").regex(keyword, "i")
                    )
            );
        }

        if (criteria.getOrganizationId() != null)
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));

        if (criteria.getBranchId() != null)
            filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));

        long total = mongoTemplate.count(query, Category.class);

        query.with(pageable);
        List<Category> categories = mongoTemplate.find(query, Category.class);
        return new PageImpl<>(categories, pageable, total);
    }
}
