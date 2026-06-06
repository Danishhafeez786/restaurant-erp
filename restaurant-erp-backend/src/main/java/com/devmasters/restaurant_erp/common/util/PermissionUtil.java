package com.devmasters.restaurant_erp.common.util;

import java.util.List;

public class PermissionUtil {

    public static boolean hasPermission(List<String> permissions, String required) {
        if (permissions == null || required == null) return false;
        return permissions.contains(required);
    }

    public static boolean hasAnyPermission(List<String> permissions, String... required) {
        if (permissions == null) return false;

        for (String perm : required) {
            if (permissions.contains(perm)) {
                return true;
            }
        }
        return false;
    }

    public static void checkPermission(List<String> permissions, String required) {
        if (!hasPermission(permissions, required)) {
            throw new RuntimeException("Access Denied: Missing permission " + required);
        }
    }
}
