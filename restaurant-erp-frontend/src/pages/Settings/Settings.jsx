import { useEffect, useMemo, useState } from "react";
import { toast } from "react-toastify";

import Sidebar from "../../components/Sidebar";

import SummaryCards from "../../components/Settings/SummaryCards";
import SettingsToolbar from "../../components/settings/SettingsToolbar";
import PermissionMatrix from "../../components/settings/PermissionMatrix";
import RoleList from "../../components/settings/RoleList";
import RolePermissionModal from "../../components/settings/RolePermissionModal";

import roleService from "../../services/roleService";
import permissionService from "../../services/permissionService";
import rolePermissionService from "../../services/rolePermissionService";
import matrixService from "../../services/matrixService";

export default function Settings() {

    const [loading, setLoading] = useState(true);

    const [roles, setRoles] = useState([]);

    const [permissions, setPermissions] = useState([]);

    const [matrix, setMatrix] = useState({
        roles: [],
        modules: [],
        assignments: []
    });

    const [rolePermissions, setRolePermissions] = useState([]);

    const [selectedRole, setSelectedRole] = useState(null);

    const [filters, setFilters] = useState({
        search: "",
        role: "",
        module: "",
        status: ""
    });

    const [modalOpen, setModalOpen] = useState(false);

    const [modalMode, setModalMode] = useState("create");

    const [selectedPermission, setSelectedPermission] = useState(null);

    useEffect(() => {

        loadData();

        const stream = rolePermissionService.stream();

        stream.onmessage = () => {
            loadMatrix();
        };

        return () => stream.close();
    }, []);

    const loadData = async () => {

        setLoading(true);

        try {

            await loadMatrix();

        } catch (e) {

            toast.error("Failed to load settings.");

        }

        setLoading(false);

    };

    const loadRoles = async () => {
        const res = await roleService.search({});
        const list = res.content || [];

        setRoles(list);

        if (list.length && !selectedRole) {
            setSelectedRole(list[0]);
        }
    };

    const loadPermissions = async () => {
        const res = await permissionService.search({});
        setPermissions(res.content || []);
    };

    const loadMatrix = async () => {

        const response = await matrixService.getMatrix();

        const data = response.data || response;

        setMatrix(data);

        // Roles
        setRoles(data.roles || []);

        // Flat Permission List (required by dropdown & filters)
        const permissionList = (data.modules || []).flatMap(module =>
            (module.permissions || []).map(permission => ({
                ...permission,
                module: module.module
            }))
        );

        setPermissions(permissionList);

        // Convert Matrix Assignments into old RolePermission structure
        const rolePermissionList = (data.assignments || []).map(assign => {

            const role = data.roles.find(r => r.id === assign.roleId);

            let permission = null;

            let moduleName = "";

            for (const module of data.modules || []) {
                const p = (module.permissions || []).find(
                    x => x.id === assign.permissionId
                );

                if (p) {
                    permission = p;
                    moduleName = module.module;
                    break;
                }
            }

            return {

                id: assign.rolePermissionId,

                roleModel: role,

                permissionModel: permission
                    ? {
                        ...permission,
                        module: moduleName
                    }
                    : null,

                isActive: assign.isActive,

                assigned: assign.assigned

            };

        });

        setRolePermissions(rolePermissionList);

        if (data.roles?.length > 0 && !selectedRole) {
            setSelectedRole(data.roles[0]);
        }
    };

    const summary = useMemo(() => {
        return {

            roles: roles.length,

            permissions: permissions.length,

            assigned: rolePermissions.filter(x => x.isActive).length,

            active: rolePermissions.filter(x => x.isActive).length

        };

    }, [roles, permissions, rolePermissions]);

    const filteredModules = useMemo(() => {

    const search = filters.search.trim().toLowerCase();

    return (matrix.modules || [])
        .map(module => {

            const permissions = module.permissions.filter(permission => {

                // Search
                if (search) {

                    const match =
                        module.module.toLowerCase().includes(search) ||
                        permission.name.toLowerCase().includes(search) ||
                        permission.code.toLowerCase().includes(search);

                    if (!match) {
                        return false;
                    }
                }

                // Module Filter
                if (filters.module && module.module !== filters.module) {
                    return false;
                }

                // Role / Status Filter
                if (filters.role || filters.status !== "") {

                    const assignments = matrix.assignments.filter(a =>
                        a.permissionId === permission.id
                    );

                    const exists = assignments.some(a => {
                        if (filters.role && a.roleId !== filters.role) {
                            return false;
                        }

                        if (filters.status !== "" && a.isActive !== (filters.status === "true")) {
                            return false;
                        }

                        return true;

                    });

                    if (!exists) {
                        return false;
                    }
                }

                return true;
            });

            return {
                ...module,
                permissions
            };

        })
        .filter(module => module.permissions.length > 0);

}, [matrix, filters]);

    const openCreate = () => {

        setSelectedPermission(null);

        setModalMode("create");

        setModalOpen(true);

    };

    const openView = (item) => {

        setSelectedPermission(item);

        setModalMode("view");

        setModalOpen(true);

    };

    const openEdit = (item) => {

        setSelectedPermission(item);

        setModalMode("edit");

        setModalOpen(true);

    };

    const savePermission = async (payload) => {

        try {

            if (modalMode === "create") {

                await rolePermissionService.create({

                    roleModel: {
                        id: payload.roleId
                    },

                    permissionModel: {
                        id: payload.permissionId
                    },

                    isActive: payload.isActive

                });

                toast.success("Permission Assigned");

            } else {

                await rolePermissionService.update(

                    selectedPermission.id,

                    {

                        id: selectedPermission.id,

                        roleModel: {
                            id: payload.roleId
                        },

                        permissionModel: {
                            id: payload.permissionId
                        },

                        isActive: payload.isActive

                    }

                );

                toast.success("Permission Updated");

            }

            setModalOpen(false);

            loadMatrix();

        } catch {

            toast.error("Operation failed");

        }

    };

    const deletePermission = async (id) => {

        if (!window.confirm("Delete this permission?")) return;

        await rolePermissionService.delete(id);

        toast.success("Permission Deleted");

        loadMatrix();

    };

    const restorePermission = async (id) => {

        await rolePermissionService.restore(id);

        toast.success("Permission Restored");

        loadMatrix();

    };

    const handlePermissionToggle = async ({

        role,

        permission,

        assignment,

        checked

    }) => {

        try {
            if (checked) {
                if (assignment.rolePermissionId === null) {
                    await rolePermissionService.create({
                        roleModel: {
                            id: role.id
                        },
                        permissionModel: {
                            id: permission.id
                        },
                        isActive: true
                    });
                } else if (!assignment.isActive) {
                    await rolePermissionService.restore(
                        assignment.rolePermissionId
                    );
                }
            } else {
                if (assignment) {
                    await rolePermissionService.delete(
                        assignment.rolePermissionId
                    );
                }
            }
            await loadMatrix();
            toast.success("Permission updated successfully.");
        } catch (error) {
            toast.error("Failed to update permission.");
        }
    };

    return (

        <div className="min-h-screen bg-gray-100 lg:flex">

            <Sidebar />

            <div className="flex-1 p-4 md:p-6 overflow-y-auto">

                <SummaryCards summary={summary} />

                <div className="mt-6">

                    <SettingsToolbar
                        roles={roles}
                        permissions={permissions}
                        filters={filters}
                        modules={filteredModules}
                        setFilters={setFilters}
                        onAdd={openCreate}
                    />

                </div>

                <div className="grid grid-cols-1 xl:grid-cols-4 gap-6 mt-6">

                    <div className="xl:col-span-3">

                        <PermissionMatrix
                            loading={loading}
                            modules={filteredModules}
                            roles={roles}
                            assignments={matrix.assignments}
                            filters={filters}
                            onPermissionToggle={handlePermissionToggle}
                        />

                    </div>

                    <RoleList

                        roles={roles}

                        rolePermissions={rolePermissions}

                        selectedRole={selectedRole}

                        setSelectedRole={setSelectedRole}

                    />

                </div>

            </div>

            {/* <RolePermissionModal

                open={modalOpen}

                mode={modalMode}

                data={selectedPermission}

                roles={roles}

                permissions={permissions}

                onClose={() => setModalOpen(false)}

                onSave={savePermission}

            /> */}

        </div>

    );

}