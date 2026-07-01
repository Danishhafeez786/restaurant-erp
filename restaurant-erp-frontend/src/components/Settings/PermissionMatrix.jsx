import { CheckIcon } from "@heroicons/react/24/outline";

export default function PermissionMatrix({

    loading,

    roles = [],

    modules = [],

    assignments = [],

    filters = {},

    onToggle

}) {

    const isAssigned = (roleId, permissionId) => {
        return assignments.find(
            item =>
                item.roleId === roleId &&
                item.permissionId === permissionId
        );
    };

    if (loading) {

        return (
            <div className="bg-white rounded-2xl shadow-md p-10 text-center">
                Loading...
            </div>
        );

    }

    const visibleRoles = roles.filter(role => {
        if (!filters.role) return true;
        return role.id === filters.role;
    });

    return (

        <div className="bg-white rounded-2xl shadow-md overflow-auto">

            <table className="min-w-full">

                <thead>

                    <tr className="bg-gray-50">

                        <th className="text-left p-4 border">

                            Module

                        </th>

                        <th className="text-left p-4 border">

                            Permission

                        </th>

                        {

                            roles
                                .filter(role => {

                                    if (filters.role) {
                                        return role.id === filters.role;
                                    }

                                    return true;

                                })
                                .map(role => (

                                    <th
                                        key={role.id}
                                        className="text-center border min-w-[120px]"
                                    >

                                        {role.roleName}

                                    </th>

                                ))

                        }

                    </tr>

                </thead>

                <tbody>

                    {

                        modules.map(module => (

                            module.permissions
                                .filter(permission => {

                                    if (filters.status === "") {
                                        return true;
                                    }

                                    const hasAssignment = assignments.some(a =>

                                        a.permissionId === permission.id &&
                                        a.isActive === (filters.status === "true")

                                    );

                                    return hasAssignment;

                                })
                                .map(permission => (

                                    <tr
                                        key={permission.id}
                                        className="border-b hover:bg-gray-50"
                                    >

                                        <td className="p-4">

                                            {module.module}

                                        </td>

                                        <td className="p-4">

                                            {permission.name}

                                        </td>

                                        {

                                            roles
                                                .filter(role => {

                                                    if (filters.role) {
                                                        return role.id === filters.role;
                                                    }

                                                    return true;

                                                })
                                                .map(role => {

                                                    const assignment =
                                                        isAssigned(
                                                            role.id,
                                                            permission.id
                                                        );

                                                    return (

                                                        <td
                                                            key={role.id}
                                                            className="text-center"
                                                        >

                                                            <input

                                                                type="checkbox"

                                                                checked={assignment?.isActive || false}

                                                                onChange={(e) =>

                                                                    onToggle({

                                                                        role,

                                                                        permission,

                                                                        assignment,

                                                                        checked: e.target.checked

                                                                    })

                                                                }

                                                                className="w-5 h-5 accent-green-700 cursor-pointer"

                                                            />

                                                        </td>

                                                    );

                                                })

                                        }

                                    </tr>

                                ))

                        ))

                    }

                </tbody>

            </table>

        </div>

    );

}