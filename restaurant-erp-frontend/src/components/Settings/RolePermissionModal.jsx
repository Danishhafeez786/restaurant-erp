import { useEffect, useState } from "react";

export default function RolePermissionModal({

    open,

    mode = "create",

    data = null,

    roles = [],

    permissions = [],

    onClose,

    onSave

}) {

    const readOnly = mode === "view";

    const [form, setForm] = useState({

        roleId: "",

        permissionId: "",

        isActive: true

    });

    useEffect(() => {

        if (data) {

            setForm({

                roleId: data.roleModel?.id || "",

                permissionId: data.permissionModel?.id || "",

                isActive: data.isActive ?? true

            });

        } else {

            setForm({

                roleId: "",

                permissionId: "",

                isActive: true

            });

        }

    }, [data, open]);

    if (!open) return null;

    const handleSubmit = () => {

        if (!form.roleId) {

            alert("Please select Role");

            return;

        }

        if (!form.permissionId) {

            alert("Please select Permission");

            return;

        }

        onSave({

            roleModel: {

                id: form.roleId

            },

            permissionModel: {

                id: form.permissionId

            },

            isActive: form.isActive

        });

    };

    return (

        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">

            <div className="bg-white rounded-2xl shadow-xl w-full max-w-2xl">

                {/* Header */}

                <div className="border-b px-6 py-4 flex justify-between items-center">

                    <h2 className="text-2xl font-bold">

                        {

                            mode === "create"

                                ? "Assign Permission"

                                : mode === "edit"

                                    ? "Edit Permission"

                                    : "View Permission"

                        }

                    </h2>

                    <button

                        onClick={onClose}

                        className="text-2xl font-bold text-gray-500 hover:text-red-600"

                    >

                        ×

                    </button>

                </div>

                {/* Body */}

                <div className="p-6 space-y-6">

                    {/* Role */}

                    <div>

                        <label className="block mb-2 font-semibold">

                            Role

                        </label>

                        <select

                            disabled={readOnly}

                            value={form.roleId}

                            onChange={(e) =>

                                setForm({

                                    ...form,

                                    roleId: e.target.value

                                })

                            }

                            className="w-full border rounded-xl px-4 py-3 focus:ring-2 focus:ring-[#0d4039] outline-none disabled:bg-gray-100"

                        >

                            <option value="">

                                Select Role

                            </option>

                            {

                                roles.map(role => (

                                    <option

                                        key={role.id}

                                        value={role.id}

                                    >

                                        {role.roleName}

                                    </option>

                                ))

                            }

                        </select>

                    </div>

                    {/* Permission */}

                    <div>

                        <label className="block mb-2 font-semibold">

                            Permission

                        </label>

                        <select

                            disabled={readOnly}

                            value={form.permissionId}

                            onChange={(e) =>

                                setForm({

                                    ...form,

                                    permissionId: e.target.value

                                })

                            }

                            className="w-full border rounded-xl px-4 py-3 focus:ring-2 focus:ring-[#0d4039] outline-none disabled:bg-gray-100"

                        >

                            <option value="">

                                Select Permission

                            </option>

                            {

                                permissions.map(permission => (

                                    <option

                                        key={permission.id}

                                        value={permission.id}

                                    >

                                        {permission.module} - {permission.name}

                                    </option>

                                ))

                            }

                        </select>

                    </div>

                    {/* Status */}

                    <div className="flex items-center gap-3">

                        <input

                            type="checkbox"

                            checked={form.isActive}

                            disabled={readOnly}

                            onChange={(e) =>

                                setForm({

                                    ...form,

                                    isActive: e.target.checked

                                })

                            }

                            className="w-5 h-5"

                        />

                        <label className="font-medium">

                            Active

                        </label>

                    </div>

                    {/* View Information */}

                    {

                        mode === "view" && data && (

                            <div className="grid md:grid-cols-2 gap-4 border-t pt-5">

                                <div>

                                    <p className="text-sm text-gray-500">

                                        Created At

                                    </p>

                                    <p className="font-medium">

                                        {data.createdAt || "-"}

                                    </p>

                                </div>

                                <div>

                                    <p className="text-sm text-gray-500">

                                        Updated At

                                    </p>

                                    <p className="font-medium">

                                        {data.updatedAt || "-"}

                                    </p>

                                </div>

                                <div>

                                    <p className="text-sm text-gray-500">

                                        Record ID

                                    </p>

                                    <p className="font-medium break-all">

                                        {data.id}

                                    </p>

                                </div>

                            </div>

                        )

                    }

                </div>

                {/* Footer */}

                <div className="border-t px-6 py-4 flex justify-end gap-3">

                    <button

                        onClick={onClose}

                        className="px-6 py-2 rounded-lg border hover:bg-gray-100"

                    >

                        Close

                    </button>

                    {

                        mode !== "view" && (

                            <button

                                onClick={handleSubmit}

                                className="px-6 py-2 rounded-lg bg-[#0d4039] hover:bg-[#14574f] text-white"

                            >

                                {

                                    mode === "create"

                                        ? "Save"

                                        : "Update"

                                }

                            </button>

                        )

                    }

                </div>

            </div>

        </div>

    );

}