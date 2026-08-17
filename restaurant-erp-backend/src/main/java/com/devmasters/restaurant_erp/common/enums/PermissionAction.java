package com.devmasters.restaurant_erp.common.enums;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionAction {

    CREATE("Create"),
    VIEW("View"),
    UPDATE("Update"),
    DELETE("Delete"),
    REACTIVATE("ReActivate");

    private final String displayName;

}