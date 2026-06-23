package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Employee;
import com.devmasters.restaurant_erp.model.EmployeeModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class EmployeeTransformer extends Transformer<Employee, EmployeeModel>{
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;
    private final UserTransformer userTransformer;

    @Override
    public Employee toEntity(EmployeeModel model) {
        if(model == null)
            return null;
        return Employee.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .employeeCode(model.getEmployeeCode())
                .fullName(model.getFullName())
                .cnic(model.getCnic())
                .phone(model.getPhone())
                .address(model.getAddress())
                .joiningDate(model.getJoiningDate())
                .salary(model.getSalary())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .user(userTransformer.toEntity(model.getUserModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public EmployeeModel toModel(Employee entity) {
        if(entity == null)
            return null;
        return EmployeeModel.builder()
                .id(entity.getId())
                .employeeCode(entity.getEmployeeCode())
                .fullName(entity.getFullName())
                .cnic(entity.getCnic())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .joiningDate(entity.getJoiningDate())
                .salary(entity.getSalary())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .userModel(userTransformer.toModel(entity.getUser()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
