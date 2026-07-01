import { UserGroupIcon } from "@heroicons/react/24/outline";

export default function RoleList({

    roles = [],

    rolePermissions = [],

    selectedRole,

    setSelectedRole

}) {

    const getAssignedCount = (roleId) => {

        return rolePermissions.filter(

            item =>

                item.roleModel?.id === roleId &&
                item.isActive

        ).length;

    };

    return (

        <div className="bg-white rounded-2xl shadow-md overflow-hidden">

            {/* Header */}

            <div className="px-5 py-4 border-b">

                <h2 className="text-xl font-bold text-gray-800">

                    Roles

                </h2>

                <p className="text-sm text-gray-500 mt-1">

                    Select a role to manage permissions.

                </p>

            </div>

            {/* Role List */}

            <div className="max-h-[650px] overflow-y-auto">

                {

                    roles.length === 0 ? (

                        <div className="text-center py-10 text-gray-500">

                            No Roles Found

                        </div>

                    ) : (

                        roles.map((role) => {

                            const active =
                                selectedRole?.id === role.id;

                            const permissionCount =
                                getAssignedCount(role.id);

                            return (

                                <div

                                    key={role.id}

                                    onClick={() => setSelectedRole(role)}

                                    className={`cursor-pointer border-b p-4 transition-all duration-200

                                    ${
                                        active
                                            ? "bg-[#0d4039] text-white"
                                            : "hover:bg-gray-50"
                                    }`}

                                >

                                    <div className="flex justify-between items-center">

                                        <div className="flex items-center gap-3">

                                            <UserGroupIcon className="w-6 h-6" />

                                            <div>

                                                <h3 className="font-semibold">

                                                    {role.roleName}

                                                </h3>

                                                <p className={`text-xs mt-1

                                                ${
                                                    active
                                                        ? "text-gray-200"
                                                        : "text-gray-500"
                                                }`}>

                                                    {role.description || "No Description"}

                                                </p>

                                            </div>

                                        </div>

                                        <span

                                            className={`px-2 py-1 rounded-full text-xs font-medium

                                            ${
                                                active
                                                    ? "bg-white text-[#0d4039]"
                                                    : role.isActive
                                                        ? "bg-green-100 text-green-700"
                                                        : "bg-red-100 text-red-700"
                                            }`}

                                        >

                                            {

                                                role.isActive
                                                    ? "Active"
                                                    : "Inactive"

                                            }

                                        </span>

                                    </div>

                                    <div className="flex justify-between items-center mt-4">

                                        <span className={`text-sm

                                        ${
                                            active
                                                ? "text-gray-200"
                                                : "text-gray-500"
                                        }`}>

                                            Assigned Permissions

                                        </span>

                                        <span className="text-2xl font-bold">

                                            {permissionCount}

                                        </span>

                                    </div>

                                </div>

                            );

                        })

                    )

                }

            </div>

        </div>

    );

}