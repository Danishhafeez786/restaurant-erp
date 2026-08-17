package com.devmasters.restaurant_erp.employee.respository;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.employee.domain.Employee;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.auth.domain.User;
import com.devmasters.restaurant_erp.employee.respository.custom.EmployeeRepositoryCustomRepository;
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
