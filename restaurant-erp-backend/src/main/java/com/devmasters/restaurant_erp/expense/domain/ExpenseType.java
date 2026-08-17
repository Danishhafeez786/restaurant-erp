package com.devmasters.restaurant_erp.expense.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("expense_types")
public class ExpenseType extends BaseEntity {

    @Indexed
    private String typeName;

    @Indexed
    private String code;

    private String description;

    private Boolean requiresApproval;

    private Boolean requiresAttachment;

    private Boolean taxable;

    @DBRef
    private Organization organization;
}
