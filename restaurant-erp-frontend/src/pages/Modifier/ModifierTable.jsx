import { useEffect, useState } from "react";
import modifierService from "../../services/modifierService";

import ModifierModelBox from "./ModifierModelBox";

export default function ModifierTable() {

    const [modifiers, setModifiers] = useState([]);

    const [loading, setLoading] = useState(false);

    const [showModal, setShowModal] = useState(false);

    const [modalMode, setModalMode] = useState("create");

    const [selectedModifier, setSelectedModifier] = useState(null);

    const [currentPage, setCurrentPage] = useState(0);

    const [totalPages, setTotalPages] = useState(0);

    const [pageSize, setPageSize] = useState(10);

    const [sortBy, setSortBy] = useState("createdAt");

    const [direction, setDirection] = useState("DESC");

    const [searchCriteria, setSearchCriteria] = useState({

        code: "",

        name: "",

        sku: "",

        modifierGroup: "",

        organization: "",

        branch: "",

        available: "",

        isActive: "",

    });

    const user = JSON.parse(localStorage.getItem("user"));

    const canCreate =
        user?.permissions?.includes("MODIFIER_CREATE");

    const canView =
        user?.permissions?.includes("MODIFIER_VIEW");

    const canUpdate =
        user?.permissions?.includes("MODIFIER_UPDATE");

    const canDelete =
        user?.permissions?.includes("MODIFIER_DELETE");

    const canRestore =
        user?.permissions?.includes("MODIFIER_REACTIVATE");

    const loadModifiers = async () => {

        try {

            setLoading(true);

            const payload = {

                code:
                    searchCriteria.code || null,

                name:
                    searchCriteria.name || null,

                sku:
                    searchCriteria.sku || null,

                modifierGroup:
                    searchCriteria.modifierGroup || null,

                organization:
                    searchCriteria.organization || null,

                branch:
                    searchCriteria.branch || null,

                available:
                    searchCriteria.available === ""
                        ? null
                        : searchCriteria.available === "true",

                isActive:
                    searchCriteria.isActive === ""
                        ? null
                        : searchCriteria.isActive === "true",

            };

            const response =
                await modifierService.search(
                    payload,
                    currentPage,
                    pageSize,
                    sortBy,
                    direction
                );

            console.log(response);

            setModifiers(
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

        loadModifiers();

    }, [
        currentPage,
        pageSize,
        sortBy,
        direction
    ]);

    useEffect(() => {
    const timer = setTimeout(() => {
        setCurrentPage(0);
        loadModifiers();
    }, 300);

    return () => clearTimeout(timer);
}, [searchCriteria, sortBy, direction, pageSize]);

    useEffect(() => {

        const eventSource =
            modifierService.stream();

        eventSource.onmessage = () => {

            loadModifiers();

        };

        eventSource.onerror = () => {

            eventSource.close();

        };

        return () => eventSource.close();

    }, []);

    const handleDelete = async (id) => {

        if (
            !window.confirm(
                "Delete this modifier?"
            )
        ) {

            return;

        }

        try {

            await modifierService.delete(id);

            alert(
                "Modifier Deleted Successfully"
            );

            loadModifiers();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to delete modifier."
            );

        }

    };

    const handleRestore = async (id) => {

        if (
            !window.confirm(
                "Restore this modifier?"
            )
        ) {

            return;

        }

        try {

            await modifierService.restore(id);

            alert(
                "Modifier Restored Successfully"
            );

            loadModifiers();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to restore modifier."
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
                    Modifier
                </h2>

                {canCreate && (
                    <button
                        onClick={() => {
                            setModalMode("create");
                            setSelectedModifier(null);
                            setShowModal(true);
                        }}
                        className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
                    >
                        + Add Modifier
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

        {/* SKU */}

        <input
            type="text"
            placeholder="SKU"
            value={searchCriteria.sku}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    sku: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        {/* Modifier Group */}

        <input
            type="text"
            placeholder="Modifier Group"
            value={searchCriteria.modifierGroup}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    modifierGroup: e.target.value,
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

        {/* Availability */}

        <select
            value={searchCriteria.available}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    available: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        >
            <option value="">Available</option>
            <option value="true">Available</option>
            <option value="false">Unavailable</option>
        </select>

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
            <option value="price">Price</option>
            <option value="displayOrder">Display Order</option>
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

                                <th className="py-3">SKU</th>

                                <th className="py-3">Price</th>

                                <th className="py-3">Cost Price</th>

                                <th className="py-3">Calories</th>

                                <th className="py-3">Display Order</th>

                                <th className="py-3">Inventory</th>

                                <th className="py-3">Available</th>

                                <th className="py-3">Modifier Group</th>

                                <th className="py-3">Organization</th>

                                <th className="py-3">Branch</th>

                                <th className="py-3">Status</th>

                                <th className="py-3">Action</th>

                            </tr>

                        </thead>

                        <tbody>

                            {modifiers.length === 0 ? (

                                <tr>

                                    <td
                                        colSpan="14"
                                        className="py-10 text-center text-gray-500"
                                    >
                                        No Modifiers Found
                                    </td>

                                </tr>

                            ) : (

                                modifiers.map((modifier) => (

                                    <tr
                                        key={modifier.id}
                                        className="border-b hover:bg-gray-50"
                                    >

                                        <td className="py-3">
                                            {modifier.code}
                                        </td>

                                        <td className="py-3 font-medium">
                                            {modifier.name}
                                        </td>

                                        <td className="py-3">
                                            {modifier.sku}
                                        </td>

                                        <td className="py-3">
                                            {modifier.price}
                                        </td>

                                        <td className="py-3">
                                            {modifier.costPrice}
                                        </td>

                                        <td className="py-3">
                                            {modifier.calories}
                                        </td>

                                        <td className="py-3">
                                            {modifier.displayOrder}
                                        </td>

                                        <td className="py-3">

                                            <span
                                                className={`px-3 py-1 rounded-full text-sm ${
                                                    modifier.inventoryTracked
                                                        ? "bg-blue-100 text-blue-700"
                                                        : "bg-gray-200 text-gray-700"
                                                }`}
                                            >
                                                {modifier.inventoryTracked
                                                    ? "Tracked"
                                                    : "Not Tracked"}
                                            </span>

                                        </td>

                                        <td className="py-3">

                                            <span
                                                className={`px-3 py-1 rounded-full text-sm ${
                                                    modifier.available
                                                        ? "bg-green-100 text-green-700"
                                                        : "bg-red-100 text-red-700"
                                                }`}
                                            >
                                                {modifier.available
                                                    ? "Available"
                                                    : "Unavailable"}
                                            </span>

                                        </td>

                                        <td className="py-3">
                                            {modifier.modifierGroupModel?.name || "-"}
                                        </td>

                                        <td className="py-3">
                                            {modifier.organizationModel?.organizationName || "-"}
                                        </td>

                                        <td className="py-3">
                                            {modifier.branchModel?.branchName || "-"}
                                        </td>

                                        <td className="py-3">

                                            <span
                                                className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                                                    modifier.isActive
                                                )}`}
                                            >
                                                {modifier.isActive
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
                                                        setSelectedModifier(modifier);
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
                                                        setSelectedModifier(modifier);
                                                        setShowModal(true);
                                                    }}
                                                >
                                                    Edit
                                                </button>
                                            )}

                                            {modifier.isActive &&
                                                canDelete && (
                                                    <button
                                                        className="text-red-600 hover:underline"
                                                        onClick={() =>
                                                            handleDelete(modifier.id)
                                                        }
                                                    >
                                                        Delete
                                                    </button>
                                                )}

                                            {!modifier.isActive &&
                                                canRestore && (
                                                    <button
                                                        className="text-orange-600 hover:underline"
                                                        onClick={() =>
                                                            handleRestore(modifier.id)
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

                ) : modifiers.length === 0 ? (

                    <div className="text-center py-10 text-gray-500">
                        No Modifiers Found
                    </div>

                ) : (

                    modifiers.map((modifier) => (

                        <div
                            key={modifier.id}
                            className="border rounded-xl p-4 shadow-sm bg-white"
                        >

                            <div className="flex justify-between items-start">

                                <div>

                                    <h3 className="font-bold text-lg">
                                        {modifier.name}
                                    </h3>

                                    <p className="text-sm text-gray-500">
                                        {modifier.code}
                                    </p>

                                </div>

                                <span
                                    className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                                        modifier.isActive
                                    )}`}
                                >
                                    {modifier.isActive
                                        ? "ACTIVE"
                                        : "INACTIVE"}
                                </span>

                            </div>

                            <div className="mt-4 space-y-2 text-sm">

                                <p><b>SKU:</b> {modifier.sku}</p>

                                <p><b>Price:</b> {modifier.price}</p>

                                <p><b>Cost Price:</b> {modifier.costPrice}</p>

                                <p><b>Calories:</b> {modifier.calories}</p>

                                <p><b>Display Order:</b> {modifier.displayOrder}</p>

                                <p><b>Inventory:</b> {modifier.inventoryTracked ? "Tracked" : "Not Tracked"}</p>

                                <p><b>Available:</b> {modifier.available ? "Yes" : "No"}</p>

                                <p><b>Modifier Group:</b> {modifier.modifierGroupModel?.name || "-"}</p>

                                <p><b>Organization:</b> {modifier.organizationModel?.organizationName || "-"}</p>

                                <p><b>Branch:</b> {modifier.branchModel?.branchName || "-"}</p>

                            </div>

                            <div className="flex flex-wrap gap-2 mt-5">

                                {canView && (
                                    <button
                                        className="flex-1 bg-green-600 text-white py-2 rounded-lg"
                                        onClick={() => {
                                            setModalMode("view");
                                            setSelectedModifier(modifier);
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
                                            setSelectedModifier(modifier);
                                            setShowModal(true);
                                        }}
                                    >
                                        Edit
                                    </button>
                                )}

                                {modifier.isActive && canDelete && (
                                    <button
                                        className="flex-1 bg-red-600 text-white py-2 rounded-lg"
                                        onClick={() => handleDelete(modifier.id)}
                                    >
                                        Delete
                                    </button>
                                )}

                                {!modifier.isActive && canRestore && (
                                    <button
                                        className="flex-1 bg-orange-600 text-white py-2 rounded-lg"
                                        onClick={() => handleRestore(modifier.id)}
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

            {/* =========================== MODIFIER MODAL =========================== */}

            <ModifierModelBox
                isOpen={showModal}
                onClose={() => setShowModal(false)}
                mode={modalMode}
                modifier={selectedModifier}
                onSuccess={loadModifiers}
            />

        </div>
    );

}