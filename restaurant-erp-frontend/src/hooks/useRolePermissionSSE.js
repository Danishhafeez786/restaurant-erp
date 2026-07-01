import { useEffect } from "react";
import rolePermissionService from "../services/rolePermissionService";

export default function useRolePermissionSSE(onRefresh) {

    useEffect(() => {

        const eventSource = rolePermissionService.stream();

        eventSource.addEventListener(
            "role-permission-created",
            () => {
                onRefresh();
            }
        );

        eventSource.addEventListener(
            "role-permission-updated",
            () => {
                onRefresh();
            }
        );

        eventSource.addEventListener(
            "role-permission-deleted",
            () => {
                onRefresh();
            }
        );

        eventSource.addEventListener(
            "role-permission-restored",
            () => {
                onRefresh();
            }
        );

        eventSource.onerror = () => {

            console.log("Role Permission SSE disconnected.");

            eventSource.close();

        };

        return () => {

            eventSource.close();

        };

    }, [onRefresh]);

}