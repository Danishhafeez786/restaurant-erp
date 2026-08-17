package com.devmasters.restaurant_erp.expense.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.common.enums.AttachmentType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("expense_attachments")
public class ExpenseAttachment extends BaseEntity {

    @DBRef
    private Expense expense;

    private AttachmentType attachmentType;

    private String fileName;

    private String originalFileName;

    private String fileUrl;

    private String contentType;

    private Long fileSize;
}