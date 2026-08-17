package com.devmasters.restaurant_erp.rolepermission.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.permission.domain.Permission;
import com.devmasters.restaurant_erp.role.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("role_permissions")
public class RolePermission extends BaseEntity {
    @DBRef
    private Organization organization;
    @DBRef
    private Role role;
    @DBRef
    private Permission permission;
}
