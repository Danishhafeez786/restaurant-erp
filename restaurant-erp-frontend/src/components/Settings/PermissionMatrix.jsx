import { CheckIcon } from "@heroicons/react/24/outline";

export default function PermissionMatrix({

    loading,

    roles = [roles],

    modules = [modules],

    assignments = [assignments],

    onToggle

}) {

    const isAssigned = (roleId, permissionId) => {

        return assignments.find(

            x =>
                x.roleId === roleId &&
                x.permissionId === permissionId &&
                x.isActive

        );

    };

    if (loading) {

        return (
            <div className="bg-white rounded-2xl shadow-md p-10 text-center">
                Loading...
            </div>
        );

    }

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

                            roles.map(role => (

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

                            module.permissions.map(permission => (

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

                                        roles.map(role => {

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

                                                        checked={!!assignment}

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