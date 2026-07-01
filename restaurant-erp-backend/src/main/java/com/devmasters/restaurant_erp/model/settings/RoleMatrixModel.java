package com.devmasters.restaurant_erp.model.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleMatrixModel {

    private UUID id;

    private String roleName;

    private Boolean isActive;

}
