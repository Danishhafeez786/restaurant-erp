package com.devmasters.restaurant_erp.expense.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("expense_statuses")
public class ExpenseStatus extends BaseEntity {

    @Indexed
    private String statusName;

    @Indexed
    private String code;

    private String description;

    private String color;

    private Integer displayOrder;

    private Boolean defaultStatus;

    @DBRef
    private Organization organization;
}