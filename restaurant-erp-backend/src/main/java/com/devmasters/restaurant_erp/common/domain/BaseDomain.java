package com.devmasters.restaurant_erp.common.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;

import java.time.Instant;



@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDomain {

    @Id
    private String id;

    //Multi Tenant
    private String tenantId;

    //Restaurant Group / Company
    private String organizationId;

    //Restaurant Branch
    private String branchId;

    @NotNull
    private ContactInfo contact;

    private Address address;

    //Auditing
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    //Soft Delete
    private Boolean deleted = false;

    private Instant deletedAt;

    private String deletedBy;

    //Optimistic Locking
    @Version
    private Long version;

    //Record Status
    private Boolean active = true;
}
