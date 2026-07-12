package com.devmasters.restaurant_erp.domain.Expense;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Organization;
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
@Document("expense_categories")
public class ExpenseCategory extends BaseEntity {

    @Indexed
    private String categoryName;

    @Indexed
    private String categoryCode;

    private String description;

    private String color;

    private String icon;

    private Integer sortOrder;

    private Boolean systemDefined;

    private Boolean active;

    @DBRef
    private Organization organization;
}