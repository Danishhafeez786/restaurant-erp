import { useEffect, useState } from "react";
import modifierGroupService from "../../services/modifierGroupService";

import ModifierGroupModelBox from "./ModifierGroupModelBox";

export default function ModifierGroupTable() {

    const [modifierGroups, setModifierGroups] = useState([]);

    const [loading, setLoading] = useState(false);

    const [showModal, setShowModal] = useState(false);

    const [modalMode, setModalMode] = useState("create");

    const [selectedModifierGroup, setSelectedModifierGroup] = useState(null);

    const [currentPage, setCurrentPage] = useState(0);

    const [totalPages, setTotalPages] = useState(0);

    const [pageSize, setPageSize] = useState(10);

    const [sortBy, setSortBy] = useState("createdAt");

    const [direction, setDirection] = useState("DESC");

    const [searchCriteria, setSearchCriteria] = useState({
        code: "",
        name: "",
        description: "",
        required: "",
        organization: "",
        branch: "",
        isActive: ""
    });

    const user = JSON.parse(localStorage.getItem("user"));

    const canCreate =
        user?.permissions?.includes("MODIFIER_GROUP_CREATE");

    const canView =
        user?.permissions?.includes("MODIFIER_GROUP_VIEW");

    const canUpdate =
        user?.permissions?.includes("MODIFIER_GROUP_UPDATE");

    const canDelete =
        user?.permissions?.includes("MODIFIER_GROUP_DELETE");

    const canRestore =
        user?.permissions?.includes("MODIFIER_GROUP_REACTIVATE");

    const loadModifierGroups = async () => {

        try {

            setLoading(true);

            const payload = {

                code:
                    searchCriteria.code || null,

                name:
                    searchCriteria.name || null,

                description:
                    searchCriteria.description || null,

                required:
                    searchCriteria.required === ""
                        ? null
                        : searchCriteria.required === "true",

                organization:
                    searchCriteria.organization || null,

                branch:
                    searchCriteria.branch || null,

                isActive:
                    searchCriteria.isActive === ""
                        ? null
                        : searchCriteria.isActive === "true"

            };

            const response =
                await modifierGroupService.search(
                    payload,
                    currentPage,
                    pageSize,
                    sortBy,
                    direction
                );

            console.log(response);

            setModifierGroups(
                response.content
            );

            setTotalPages(
                response.totalPages
            );

        } catch (error) {

            console.error(error);

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => {

        loadModifierGroups();

    }, [
        currentPage,
        pageSize,
        sortBy,
        direction
    ]);

    useEffect(() => {
    const timer = setTimeout(() => {
        setCurrentPage(0);
        loadModifierGroups();
    }, 300);

    return () => clearTimeout(timer);
}, [searchCriteria, sortBy, direction, pageSize]);

    useEffect(() => {

        const eventSource =
            modifierGroupService.stream();

        eventSource.onmessage = () => {

            loadModifierGroups();

        };

        eventSource.onerror = () => {

            eventSource.close();

        };

        return () => eventSource.close();

    }, []);

    const handleDelete = async (id) => {

        if (
            !window.confirm(
                "Delete this modifier group?"
            )
        ) {

            return;

        }

        try {

            await modifierGroupService.delete(id);

            alert(
                "Modifier Group Deleted Successfully"
            );

            loadModifierGroups();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to delete modifier group."
            );

        }

    };

    const handleRestore = async (id) => {

        if (
            !window.confirm(
                "Restore this modifier group?"
            )
        ) {

            return;

        }

        try {

            await modifierGroupService.restore(id);

            alert(
                "Modifier Group Restored Successfully"
            );

            loadModifierGroups();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to restore modifier group."
            );

        }

    };

    const getStatusColor = (status) =>
        status
            ? "bg-green-100 text-green-700"
            : "bg-red-100 text-red-700";

    return (
        <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">

            {/* =========================== HEADER =========================== */}

            <div className="flex justify-between items-center mb-5">
                <h2 className="text-2xl font-bold">
                    Modifier Group
                </h2>

                {true && (
                    <button
                        onClick={() => {
                            setModalMode("create");
                            setSelectedModifierGroup(null);
                            setShowModal(true);
                        }}
                        className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
                    >
                        + Add Modifier Group
                    </button>
                )}
            </div>

            {/* =========================== SEARCH PANEL =========================== */}

            <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">

    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5">

        {/* Code */}

        <input
            type="text"
            placeholder="Code"
            value={searchCriteria.code}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    code: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        {/* Name */}

        <input
            type="text"
            placeholder="Name"
            value={searchCriteria.name}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    name: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        {/* Description */}

        <input
            type="text"
            placeholder="Description"
            value={searchCriteria.description}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    description: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

       

        {/* Organization */}

        <input
            type="text"
            placeholder="Organization"
            value={searchCriteria.organization}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    organization: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        {/* Branch */}

        <input
            type="text"
            placeholder="Branch"
            value={searchCriteria.branch}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    branch: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        {/* Status */}

        <select
            value={searchCriteria.isActive}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    isActive: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        >
            <option value="">Status</option>
            <option value="true">Active</option>
            <option value="false">Inactive</option>
        </select>

         {/* Required */}

        <select
            value={searchCriteria.required}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    required: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        >
            <option value="">Required</option>
            <option value="true">Required</option>
            <option value="false">Optional</option>
        </select>

        {/* Sort By */}

        <select
            value={sortBy}
            onChange={(e) => {
                setCurrentPage(0);
                setSortBy(e.target.value);
            }}
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        >
            <option value="createdAt">Created Date</option>
            <option value="code">Code</option>
            <option value="name">Name</option>
            <option value="minSelection">Min Selection</option>
            <option value="maxSelection">Max Selection</option>
        </select>

        {/* Direction */}

        <select
            value={direction}
            onChange={(e) => {
                setCurrentPage(0);
                setDirection(e.target.value);
            }}
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        >
            <option value="DESC">Newest</option>
            <option value="ASC">Oldest</option>
        </select>

        {/* Page Size */}

        <select
            value={pageSize}
            onChange={(e) => {
                setCurrentPage(0);
                setPageSize(Number(e.target.value));
            }}
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        >
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
            <option value={100}>100</option>
        </select>

    </div>

</div>

            {/* =========================== DESKTOP TABLE =========================== */}

            <div className="hidden md:block overflow-x-auto">

                {loading ? (
                    <div className="text-center py-10">
                        Loading...
                    </div>
                ) : (
                    <table className="w-full">

                        <thead>

                            <tr className="border-b text-left">

                                <th className="py-3">Code</th>

                                <th className="py-3">Name</th>

                                <th className="py-3">Description</th>

                                <th className="py-3">Min Selection</th>

                                <th className="py-3">Max Selection</th>

                                <th className="py-3">Required</th>

                                <th className="py-3">Organization</th>

                                <th className="py-3">Branch</th>

                                <th className="py-3">Status</th>

                                <th className="py-3">Action</th>

                            </tr>

                        </thead>

                        <tbody>

                            {modifierGroups.length === 0 ? (

                                <tr>

                                    <td
                                        colSpan="10"
                                        className="py-10 text-center text-gray-500"
                                    >
                                        No Modifier Groups Found
                                    </td>

                                </tr>

                            ) : (

                                modifierGroups.map((modifierGroup) => (

                                    <tr
                                        key={modifierGroup.id}
                                        className="border-b hover:bg-gray-50"
                                    >

                                        <td className="py-3">
                                            {modifierGroup.code}
                                        </td>

                                        <td className="py-3 font-medium">
                                            {modifierGroup.name}
                                        </td>

                                        <td className="py-3">
                                            {modifierGroup.description || "-"}
                                        </td>

                                        <td className="py-3">
                                            {modifierGroup.minSelection}
                                        </td>

                                        <td className="py-3">
                                            {modifierGroup.maxSelection}
                                        </td>

                                        <td className="py-3">

                                            <span
                                                className={`px-3 py-1 rounded-full text-sm ${
                                                    modifierGroup.required
                                                        ? "bg-blue-100 text-blue-700"
                                                        : "bg-gray-200 text-gray-700"
                                                }`}
                                            >
                                                {modifierGroup.required
                                                    ? "Required"
                                                    : "Optional"}
                                            </span>

                                        </td>

                                        <td className="py-3">
                                            {modifierGroup.organizationModel
                                                ?.organizationName || "-"}
                                        </td>

                                        <td className="py-3">
                                            {modifierGroup.branchModel
                                                ?.branchName || "-"}
                                        </td>

                                        <td className="py-3">

                                            <span
                                                className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                                                    modifierGroup.isActive
                                                )}`}
                                            >
                                                {modifierGroup.isActive
                                                    ? "ACTIVE"
                                                    : "INACTIVE"}
                                            </span>

                                        </td>

                                        <td className="py-3 space-x-3">

                                            {canView && (
                                                <button
                                                    className="text-green-600 hover:underline"
                                                    onClick={() => {
                                                        setModalMode("view");
                                                        setSelectedModifierGroup(modifierGroup);
                                                        setShowModal(true);
                                                    }}
                                                >
                                                    View
                                                </button>
                                            )}

                                            {canUpdate && (
                                                <button
                                                    className="text-blue-600 hover:underline"
                                                    onClick={() => {
                                                        setModalMode("edit");
                                                        setSelectedModifierGroup(modifierGroup);
                                                        setShowModal(true);
                                                    }}
                                                >
                                                    Edit
                                                </button>
                                            )}

                                            {modifierGroup.isActive &&
                                                canDelete && (
                                                    <button
                                                        className="text-red-600 hover:underline"
                                                        onClick={() =>
                                                            handleDelete(modifierGroup.id)
                                                        }
                                                    >
                                                        Delete
                                                    </button>
                                                )}

                                            {!modifierGroup.isActive &&
                                                canRestore && (
                                                    <button
                                                        className="text-orange-600 hover:underline"
                                                        onClick={() =>
                                                            handleRestore(modifierGroup.id)
                                                        }
                                                    >
                                                        Restore
                                                    </button>
                                                )}

                                        </td>

                                    </tr>

                                ))

                            )}

                        </tbody>

                    </table>

                )}

            </div>

            {/* =========================== MOBILE CARDS =========================== */}

            <div className="md:hidden space-y-4">

                {loading ? (

                    <div className="text-center py-10">
                        Loading...
                    </div>

                ) : modifierGroups.length === 0 ? (

                    <div className="text-center py-10 text-gray-500">
                        No Modifier Groups Found
                    </div>

                ) : (

                    modifierGroups.map((modifierGroup) => (

                        <div
                            key={modifierGroup.id}
                            className="border rounded-xl p-4 shadow-sm bg-white"
                        >

                            <div className="flex justify-between items-start">

                                <div>

                                    <h3 className="font-bold text-lg">
                                        {modifierGroup.name}
                                    </h3>

                                    <p className="text-sm text-gray-500">
                                        {modifierGroup.code}
                                    </p>

                                </div>

                                <span
                                    className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                                        modifierGroup.isActive
                                    )}`}
                                >
                                    {modifierGroup.isActive
                                        ? "ACTIVE"
                                        : "INACTIVE"}
                                </span>

                            </div>

                            <div className="mt-4 space-y-2 text-sm">

                                <p>
                                    <b>Description:</b>{" "}
                                    {modifierGroup.description || "-"}
                                </p>

                                <p>
                                    <b>Min Selection:</b>{" "}
                                    {modifierGroup.minSelection}
                                </p>

                                <p>
                                    <b>Max Selection:</b>{" "}
                                    {modifierGroup.maxSelection}
                                </p>

                                <p>
                                    <b>Required:</b>{" "}
                                    {modifierGroup.required
                                        ? "Yes"
                                        : "No"}
                                </p>

                                <p>
                                    <b>Organization:</b>{" "}
                                    {modifierGroup.organizationModel
                                        ?.organizationName || "-"}
                                </p>

                                <p>
                                    <b>Branch:</b>{" "}
                                    {modifierGroup.branchModel
                                        ?.branchName || "-"}
                                </p>

                            </div>

                            <div className="flex flex-wrap gap-2 mt-5">

                                {canView && (
                                    <button
                                        className="flex-1 bg-green-600 text-white py-2 rounded-lg"
                                        onClick={() => {
                                            setModalMode("view");
                                            setSelectedModifierGroup(modifierGroup);
                                            setShowModal(true);
                                        }}
                                    >
                                        View
                                    </button>
                                )}

                                {canUpdate && (
                                    <button
                                        className="flex-1 bg-blue-600 text-white py-2 rounded-lg"
                                        onClick={() => {
                                            setModalMode("edit");
                                            setSelectedModifierGroup(modifierGroup);
                                            setShowModal(true);
                                        }}
                                    >
                                        Edit
                                    </button>
                                )}

                                {modifierGroup.isActive &&
                                    canDelete && (
                                        <button
                                            className="flex-1 bg-red-600 text-white py-2 rounded-lg"
                                            onClick={() =>
                                                handleDelete(modifierGroup.id)
                                            }
                                        >
                                            Delete
                                        </button>
                                    )}

                                {!modifierGroup.isActive &&
                                    canRestore && (
                                        <button
                                            className="flex-1 bg-orange-600 text-white py-2 rounded-lg"
                                            onClick={() =>
                                                handleRestore(modifierGroup.id)
                                            }
                                        >
                                            Restore
                                        </button>
                                    )}

                            </div>

                        </div>

                    ))

                )}

            </div>


            {/* =========================== PAGINATION =========================== */}

             <div className="flex justify-center gap-2 mt-5">
                {[...Array(totalPages)].map((_, i) => (
                    <button
                        key={i}
                        onClick={() => setCurrentPage(i)}
                        className={`px-3 py-1 rounded ${currentPage === i ? "bg-[#0d4039] text-white" : "bg-gray-200"}`}
                    >
                        {i + 1}
                    </button>
                ))}
            </div>

            {/* =========================== MODIFIER GROUP MODAL =========================== */}

            <ModifierGroupModelBox
                isOpen={showModal}
                onClose={() => setShowModal(false)}
                mode={modalMode}
                modifierGroup={selectedModifierGroup}
                onSuccess={loadModifierGroups}
            />

        </div>
    );

}