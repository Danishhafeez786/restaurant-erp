package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Employee;
import com.devmasters.restaurant_erp.model.searchcriteria.EmployeeSearchCriteria;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryCustomRepositoryImpl implements EmployeeRepositoryCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public PageResponse<Employee> search(EmployeeSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        // GLOBAL SEARCH
        if (criteria.getSearch() != null && !criteria.getSearch().isBlank()) {

            String search = criteria.getSearch();

            Criteria searchCriteria = new Criteria().orOperator(
                            Criteria.where("employeeCode").regex(search, "i"),
                            Criteria.where("fullName").regex(search, "i"),
                            Criteria.where("phone").regex(search, "i"),
                            Criteria.where("cnic").regex(search, "i")
                    );
            filters.add(searchCriteria);
        }

        if (criteria.getRoleId() != null)
            filters.add(Criteria.where("role.$id").is(criteria.getRoleId()));

        if (criteria.getOrganizationId() != null)
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));

        if (criteria.getBranchId() != null)
            filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));

        if (criteria.getEmploymentStatus() != null)
            filters.add(Criteria.where("employmentStatus").is(criteria.getIsActive()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));

        long total = mongoTemplate.count(query, Employee.class);

        query.with(pageable);

        List<Employee> employees = mongoTemplate.find(query, Employee.class);

        return PageResponse.<Employee>builder()
                .content(employees)
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / pageable.getPageSize()))
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .first(pageable.getPageNumber() == 0)
                .last((long) (pageable.getPageNumber() + 1) * pageable.getPageSize() >= total)
                .build();
    }
}
