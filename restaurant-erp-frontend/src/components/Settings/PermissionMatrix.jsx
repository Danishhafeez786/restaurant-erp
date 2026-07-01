import {
    EyeIcon,
    PencilSquareIcon,
    TrashIcon,
    ArrowPathIcon,
} from "@heroicons/react/24/outline";

export default function PermissionMatrix({

    data = [],

    onView,

    onEdit,

    onDelete,

    onRestore

}) {

    return (

        <div className="bg-white rounded-2xl shadow-md overflow-hidden">

            {/* Header */}

            <div className="border-b px-6 py-5">

                <h2 className="text-2xl font-bold text-gray-800">

                    Permission Matrix

                </h2>

                <p className="text-gray-500 text-sm mt-1">

                    Manage role permissions.

                </p>

            </div>

            {/* Desktop */}

            <div className="hidden lg:block overflow-x-auto">

                <table className="w-full">

                    <thead>

                        <tr className="bg-gray-50 border-b">

                            <th className="text-left px-6 py-4">

                                Module

                            </th>

                            <th className="text-left">

                                Permission

                            </th>

                            <th className="text-left">

                                Role

                            </th>

                            <th className="text-center">

                                Status

                            </th>

                            <th className="text-center">

                                Actions

                            </th>

                        </tr>

                    </thead>

                    <tbody>

                        {

                            data.length === 0 ?

                                (

                                    <tr>

                                        <td
                                            colSpan={5}
                                            className="text-center py-12 text-gray-500"
                                        >

                                            No Permission Found

                                        </td>

                                    </tr>

                                )

                                :

                                (

                                    data.map((item) => (

                                        <tr

                                            key={item.id}

                                            className="border-b hover:bg-gray-50"

                                        >

                                            <td className="px-6 py-4 font-semibold">

                                                {item.permissionModel?.module}

                                            </td>

                                            <td>

                                                {item.permissionModel?.name}

                                            </td>

                                            <td>

                                                {item.roleModel?.roleName}

                                            </td>

                                            <td className="text-center">

                                                <span

                                                    className={`px-3 py-1 rounded-full text-xs font-semibold

                                                    ${

                                                        item.isActive

                                                            ? "bg-green-100 text-green-700"

                                                            : "bg-red-100 text-red-700"

                                                    }`}

                                                >

                                                    {

                                                        item.isActive

                                                            ? "Assigned"

                                                            : "Deleted"

                                                    }

                                                </span>

                                            </td>

                                            <td>

                                                <div className="flex justify-center gap-3">

                                                    <button

                                                        onClick={() => onView(item)}

                                                        className="text-green-600 hover:text-green-800"

                                                    >

                                                        <EyeIcon className="w-5 h-5"/>

                                                    </button>

                                                    <button

                                                        onClick={() => onEdit(item)}

                                                        className="text-blue-600 hover:text-blue-800"

                                                    >

                                                        <PencilSquareIcon className="w-5 h-5"/>

                                                    </button>

                                                    {

                                                        item.isActive ?

                                                            (

                                                                <button

                                                                    onClick={() => onDelete(item)}

                                                                    className="text-red-600 hover:text-red-800"

                                                                >

                                                                    <TrashIcon className="w-5 h-5"/>

                                                                </button>

                                                            )

                                                            :

                                                            (

                                                                <button

                                                                    onClick={() => onRestore(item)}

                                                                    className="text-orange-600 hover:text-orange-800"

                                                                >

                                                                    <ArrowPathIcon className="w-5 h-5"/>

                                                                </button>

                                                            )

                                                    }

                                                </div>

                                            </td>

                                        </tr>

                                    ))

                                )

                        }

                    </tbody>

                </table>

            </div>

            {/* Mobile */}

            <div className="lg:hidden p-4 space-y-4">

                {

                    data.length === 0 ?

                        (

                            <div className="text-center py-8 text-gray-500">

                                No Permission Found

                            </div>

                        )

                        :

                        (

                            data.map((item) => (

                                <div

                                    key={item.id}

                                    className="border rounded-xl p-4"

                                >

                                    <div className="flex justify-between">

                                        <div>

                                            <h3 className="font-bold">

                                                {item.permissionModel?.module}

                                            </h3>

                                            <p className="text-sm text-gray-500">

                                                {item.roleModel?.roleName}

                                            </p>

                                        </div>

                                        <span

                                            className={`px-3 py-1 rounded-full text-xs

                                            ${

                                                item.isActive

                                                    ? "bg-green-100 text-green-700"

                                                    : "bg-red-100 text-red-700"

                                            }`}

                                        >

                                            {

                                                item.isActive

                                                    ? "Assigned"

                                                    : "Deleted"

                                            }

                                        </span>

                                    </div>

                                    <div className="mt-3">

                                        <p>

                                            <b>Permission : </b>

                                            {item.permissionModel?.name}

                                        </p>

                                    </div>

                                    <div className="flex justify-between mt-5">

                                        <button

                                            onClick={() => onView(item)}

                                            className="text-green-600"

                                        >

                                            <EyeIcon className="w-6 h-6"/>

                                        </button>

                                        <button

                                            onClick={() => onEdit(item)}

                                            className="text-blue-600"

                                        >

                                            <PencilSquareIcon className="w-6 h-6"/>

                                        </button>

                                        {

                                            item.isActive ?

                                                (

                                                    <button

                                                        onClick={() => onDelete(item)}

                                                        className="text-red-600"

                                                    >

                                                        <TrashIcon className="w-6 h-6"/>

                                                    </button>

                                                )

                                                :

                                                (

                                                    <button

                                                        onClick={() => onRestore(item)}

                                                        className="text-orange-600"

                                                    >

                                                        <ArrowPathIcon className="w-6 h-6"/>

                                                    </button>

                                                )

                                        }

                                    </div>

                                </div>

                            ))

                        )

                }

            </div>

        </div>

    );

}