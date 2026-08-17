package com.devmasters.restaurant_erp.permission.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("permissions")
public class Permission extends BaseEntity {
    private String code;
    private String name;
    private String module;
}
