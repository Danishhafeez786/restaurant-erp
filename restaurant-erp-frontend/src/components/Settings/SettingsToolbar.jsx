import { PlusIcon, MagnifyingGlassIcon } from "@heroicons/react/24/outline";

export default function SettingsToolbar({

    roles = [],
    permissions = [],
    filters = {
        search: "",
        role: "",
        module: "",
        status: ""
    },
    setFilters = () => {},
    onAdd

}) {

    // Unique Modules
    const modules = [...new Set(
        permissions.map(p => p.module).filter(Boolean)
    )];

    return (

        <div className="bg-white rounded-2xl shadow-md p-5">
            <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">

                <div>

                    <h2 className="text-2xl font-bold text-gray-800">
                        Role Permissions
                    </h2>

                    <p className="text-gray-500 text-sm mt-1">
                        Manage permissions assigned to each role.
                    </p>

                </div>

                <button
                    onClick={onAdd}
                    className="flex items-center gap-2 bg-[#0d4039] hover:bg-[#14574f] text-white px-5 py-3 rounded-xl"
                >
                    <PlusIcon className="w-5 h-5" />
                    Add Permission
                </button>

            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-5 gap-4 mt-6">

                {/* Search */}

                <div className="relative xl:col-span-2">

                    <MagnifyingGlassIcon className="w-5 h-5 absolute left-3 top-3 text-gray-400" />

                    <input
                        type="text"
                        placeholder="Search..."
                        value={filters.search}
                        onChange={(e) =>
                            setFilters({
                                ...filters,
                                search: e.target.value
                            })
                        }
                        className="w-full border rounded-xl pl-10 pr-4 py-3"
                    />

                </div>

                {/* Roles */}

                <select
                    value={filters.role}
                    onChange={(e) =>
                        setFilters({
                            ...filters,
                            role: e.target.value
                        })
                    }
                    className="border rounded-xl px-4 py-3"
                >

                    <option value="">
                        All Roles
                    </option>

                    {roles.map(role => (

                        <option
                            key={role.id}
                            value={role.id}
                        >
                            {role.roleName}
                        </option>

                    ))}

                </select>

                {/* Modules */}

                <select
                    value={filters.module}
                    onChange={(e) =>
                        setFilters({
                            ...filters,
                            module: e.target.value
                        })
                    }
                    className="border rounded-xl px-4 py-3"
                >

                    <option value="">
                        All Modules
                    </option>

                    {modules.map(module => (

                        <option
                            key={module}
                            value={module}
                        >
                            {module}
                        </option>

                    ))}

                </select>

                {/* Status */}

                <select
                    value={filters.status}
                    onChange={(e) =>
                        setFilters({
                            ...filters,
                            status: e.target.value
                        })
                    }
                    className="border rounded-xl px-4 py-3"
                >

                    <option value="">
                        All Status
                    </option>

                    <option value="true">
                        Active
                    </option>

                    <option value="false">
                        Inactive
                    </option>

                </select>

            </div>

        </div>

    );

}