package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Employee;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.model.empoyee.EmployeeSearchCriteria;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.repository.custom.EmployeeRepositoryCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, UUID>, EmployeeRepositoryCustomRepository {

    Optional<Employee> findByIdAndIsActiveTrue(UUID id);

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByCnic(String cnic);

    Optional<Employee> findByPhone(String phone);

    Optional<Employee> findByUser(User user);

    Optional<Employee> findByUserId(UUID userId);

    List<Employee> findAllByOrganization(Organization organization);

    List<Employee> findAllByBranch(Branch branch);

    List<Employee> findAllByOrganizationAndIsActiveTrue(Organization organization);

    List<Employee> findAllByBranchAndIsActiveTrue(Branch branch);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByCnic(String cnic);

    boolean existsByPhone(String phone);

    boolean existsByUser(User user);

    Optional<Employee> findTopByOrderByEmployeeCodeDesc();
}
