package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Employee;
import com.devmasters.restaurant_erp.model.empoyee.EmployeeSearchCriteria;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import org.springframework.data.domain.Pageable;

public interface EmployeeRepositoryCustomRepository {

    PageResponse<Employee> search(EmployeeSearchCriteria criteria, Pageable pageable);

}
