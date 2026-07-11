package com.devmasters.restaurant_erp.service.Sequence;

import com.devmasters.restaurant_erp.enums.MenuItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private final SequenceGeneratorService sequenceGeneratorService;

    public String generateCategoryCode() {

        long sequence = sequenceGeneratorService.generateSequence("CATEGORY");

        return "CAT" + String.format("%05d", sequence);
    }


    public String generateMenuItemCode(MenuItemType type) {

        long sequence = sequenceGeneratorService.generateSequence("MENU_ITEM");

        return getPrefix(type) + String.format("%05d", sequence);
    }

    private String getPrefix(MenuItemType type) {

        return switch (type) {

            case FOOD -> "FOD";

            case DRINK -> "DRK";

            case DESSERT -> "DES";

            case APPETIZER -> "APP";

            case SALAD -> "SAL";

            case SOUP -> "SOU";

            case COMBO -> "COM";

            case SIDE -> "SID";

            case ADDON -> "ADD";
        };
    }

    public String generateEmployeeCode(UUID organizationId) {

        String sequenceName = "EMPLOYEE_" + organizationId;

        long sequence = sequenceGeneratorService.generateSequence(sequenceName);

        return "EMP" + String.format("%05d", sequence);
    }

    public String generateMenuVariantCode(UUID branchId) {

        String sequenceName = "MENU_VARIANT_" + branchId;

        long sequence = sequenceGeneratorService.generateSequence(sequenceName);

        return "VAR" + String.format("%05d", sequence);
    }

    public String generateModifierGroupCode(UUID branchId) {

        String sequenceName = "MODIFIER_GROUP_" + branchId;

        long sequence = sequenceGeneratorService.generateSequence(sequenceName);

        return "MGP" + String.format("%05d", sequence);
    }

    public String generateModifierCode(UUID branchId) {

        String sequenceName = "MODIFIER_" + branchId;

        long sequence =
                sequenceGeneratorService.generateSequence(sequenceName);

        return "MOD" + String.format("%05d", sequence);
    }
}
