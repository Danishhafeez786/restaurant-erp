package com.devmasters.restaurant_erp.role.domain;

import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import com.devmasters.restaurant_erp.common.enums.RoleType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "roles")
public class Role extends BaseDomain {

    private RoleType roleName;

    private String description;

    private List<String> permissions;
}