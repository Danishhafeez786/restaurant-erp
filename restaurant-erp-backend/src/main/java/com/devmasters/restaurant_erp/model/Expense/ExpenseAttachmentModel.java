package com.devmasters.restaurant_erp.model.Expense;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseAttachmentModel {

    private UUID id;

    private UUID expenseId;

    private String attachmentType;

    private String fileName;

    private String originalFileName;

    private String fileUrl;

    private String contentType;

    private Long fileSize;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}