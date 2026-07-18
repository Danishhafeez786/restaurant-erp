import { useEffect, useState } from "react";
import categoryService from "../../services/categoryService";

import CategoryModelBox from "./CategoryModelBox";

export default function CategoryTable() {

    const [categories, setCategories] = useState([]);

    const [loading, setLoading] = useState(false);

    const [showModal, setShowModal] = useState(false);

    const [modalMode, setModalMode] = useState("create");

    const [selectedCategory, setSelectedCategory] = useState(null);

    const [currentPage, setCurrentPage] = useState(0);

    const [totalPages, setTotalPages] = useState(0);

    const [pageSize, setPageSize] = useState(10);

    const [sortBy, setSortBy] = useState("createdAt");

    const [direction, setDirection] = useState("DESC");

    const [searchCriteria, setSearchCriteria] = useState({
        categoryCode: "",
        categoryName: "",
        description: "",
        organization: "",
        branch: "",
        available: "",
        isActive: ""
    });

    const user = JSON.parse(localStorage.getItem("user"));

    const canCreate = user?.permissions?.includes("CATEGORY_CREATE");

    const canView = user?.permissions?.includes("CATEGORY_VIEW");

    const canUpdate = user?.permissions?.includes("CATEGORY_UPDATE");

    const canDelete = user?.permissions?.includes("CATEGORY_DELETE");

    const canRestore = user?.permissions?.includes("CATEGORY_REACTIVATE");

    const loadCategories = async () => {

        try {

            setLoading(true);

            const payload = {

                categoryCode: searchCriteria.categoryCode || null,

                categoryName: searchCriteria.categoryName || null,

                description: searchCriteria.description || null,

                organization: searchCriteria.organization || null,

                branch: searchCriteria.branch || null,

                available:
                    searchCriteria.available === ""
                        ? null
                        : searchCriteria.available === "true",

                isActive:
                    searchCriteria.isActive === ""
                        ? null
                        : searchCriteria.isActive === "true"

            };

            const response = await categoryService.search(
                payload,
                currentPage,
                pageSize,
                sortBy,
                direction
            );

            console.log(response);

            setCategories(response.content);

            setTotalPages(response.totalPages);

        } catch (error) {

            console.error(error);

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => {

        loadCategories();

    }, [
        currentPage,
        pageSize,
        sortBy,
        direction
    ]);

    useEffect(() => {
    const timer = setTimeout(() => {
        setCurrentPage(0);
        loadCategories();
    }, 300);

    return () => clearTimeout(timer);
}, [searchCriteria, sortBy, direction, pageSize]);

    useEffect(() => {

        const eventSource = categoryService.stream();

        eventSource.onmessage = () => {

            loadCategories();

        };

        eventSource.onerror = () => {

            eventSource.close();

        };

        return () => eventSource.close();

    }, []);

    const handleDelete = async (id) => {

        if (!window.confirm("Delete this category?")) {

            return;

        }

        try {

            await categoryService.delete(id);

            alert("Category Deleted Successfully");

            loadCategories();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to delete category."
            );

        }

    };

    const handleRestore = async (id) => {

        if (!window.confirm("Restore this category?")) {

            return;

        }

        try {

            await categoryService.restore(id);

            alert("Category Restored Successfully");

            loadCategories();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to restore category."
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
                    Categories
                </h2>

                {canCreate && (
                    <button
                        onClick={() => {
                            setModalMode("create");
                            setSelectedCategory(null);
                            setShowModal(true);
                        }}
                        className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
                    >
                        + Add Category
                    </button>
                )}
            </div>

            {/* =========================== SEARCH PANEL =========================== */}

           <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">

    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5">

        {/* Category Code */}

        <input
            type="text"
            placeholder="Category Code"
            value={searchCriteria.categoryCode}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    categoryCode: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        {/* Category Name */}

        <input
            type="text"
            placeholder="Category Name"
            value={searchCriteria.categoryName}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    categoryName: e.target.value,
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
            <option value="">Availability</option>
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
            <option value="createdAt">Created</option>
            <option value="categoryCode">Category Code</option>
            <option value="categoryName">Category Name</option>
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

                                <th className="py-3">Category</th>

                                <th className="py-3">Description</th>

                                <th className="py-3">Display Order</th>

                                <th className="py-3">Available</th>

                                <th className="py-3">Organization</th>

                                <th className="py-3">Branch</th>

                                <th className="py-3">Status</th>

                                <th className="py-3">Action</th>

                            </tr>
                        </thead>

                        <tbody>

                            {categories.length === 0 ? (

                                <tr>

                                    <td
                                        colSpan="9"
                                        className="py-10 text-center text-gray-500"
                                    >
                                        No Categories Found
                                    </td>

                                </tr>

                            ) : (

                                categories.map((category) => (

                                    <tr
                                        key={category.id}
                                        className="border-b hover:bg-gray-50"
                                    >

                                        <td className="py-3">
                                            {category.categoryCode}
                                        </td>

                                        <td className="py-3">

                                            <div className="flex items-center gap-3">

                                                {category.imageUrl ? (
                                                    <img
                                                        src={category.imageUrl}
                                                        alt={category.categoryName}
                                                        className="w-12 h-12 rounded-lg border object-cover"
                                                    />
                                                ) : (
                                                    <div className="w-12 h-12 rounded-lg border flex items-center justify-center bg-gray-100 text-gray-400 text-xs">
                                                        No Image
                                                    </div>
                                                )}

                                                <div>

                                                    <div className="font-medium">
                                                        {category.categoryName}
                                                    </div>

                                                    <div className="text-xs text-gray-500">
                                                        {category.categoryCode}
                                                    </div>

                                                </div>

                                            </div>

                                        </td>

                                        <td className="py-3">
                                            {category.description || "-"}
                                        </td>

                                        <td className="py-3">
                                            {category.displayOrder}
                                        </td>

                                        <td className="py-3">

                                            <span
                                                className={`px-3 py-1 rounded-full text-sm ${
                                                    category.available
                                                        ? "bg-blue-100 text-blue-700"
                                                        : "bg-gray-200 text-gray-700"
                                                }`}
                                            >
                                                {category.available
                                                    ? "Available"
                                                    : "Unavailable"}
                                            </span>

                                        </td>

                                        <td className="py-3">
                                            {category.organizationModel
                                                ?.organizationName || "-"}
                                        </td>

                                        <td className="py-3">
                                            {category.branchModel
                                                ?.branchName || "-"}
                                        </td>

                                        <td className="py-3">

                                            <span
                                                className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                                                    category.isActive
                                                )}`}
                                            >
                                                {category.isActive
                                                    ? "ACTIVE"
                                                    : "INACTIVE"}
                                            </span>

                                        </td>

                                        <td className="py-3 space-x-3">

                                            {canView && (
                                                <button
                                                    className="text-green-600"
                                                    onClick={() => {
                                                        setModalMode("view");
                                                        setSelectedCategory(category);
                                                        setShowModal(true);
                                                    }}
                                                >
                                                    View
                                                </button>
                                            )}

                                            {canUpdate && (
                                                <button
                                                    className="text-blue-600"
                                                    onClick={() => {
                                                        setModalMode("edit");
                                                        setSelectedCategory(category);
                                                        setShowModal(true);
                                                    }}
                                                >
                                                    Edit
                                                </button>
                                            )}

                                            {category.isActive &&
                                                canDelete && (
                                                    <button
                                                        className="text-red-600"
                                                        onClick={() =>
                                                            handleDelete(category.id)
                                                        }
                                                    >
                                                        Delete
                                                    </button>
                                                )}

                                            {!category.isActive &&
                                                canRestore && (
                                                    <button
                                                        className="text-orange-600"
                                                        onClick={() =>
                                                            handleRestore(category.id)
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

            {/* =========================== MOBILE VIEW =========================== */}

            <div className="md:hidden space-y-4">

                {categories.length === 0 ? (

                    <div className="text-center py-10 text-gray-500">
                        No Categories Found
                    </div>

                ) : (

                    categories.map((category) => (

                        <div
                            key={category.id}
                            className="border rounded-xl p-4 bg-white shadow-sm"
                        >

                            <div className="flex justify-between items-start">

                                <div className="flex gap-3">

                                    {category.imageUrl ? (
                                        <img
                                            src={category.imageUrl}
                                            alt={category.categoryName}
                                            className="w-16 h-16 rounded-lg border object-cover"
                                        />
                                    ) : (
                                        <div className="w-16 h-16 rounded-lg border flex items-center justify-center bg-gray-100 text-xs text-gray-400">
                                            No Image
                                        </div>
                                    )}

                                    <div>

                                        <h3 className="font-bold">
                                            {category.categoryName}
                                        </h3>

                                        <p className="text-xs text-gray-500">
                                            {category.categoryCode}
                                        </p>

                                    </div>

                                </div>

                                <span
                                    className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                                        category.isActive
                                    )}`}
                                >
                                    {category.isActive
                                        ? "ACTIVE"
                                        : "INACTIVE"}
                                </span>

                            </div>

                            <div className="mt-4 space-y-2 text-sm">

                                <p>
                                    <b>Description:</b>{" "}
                                    {category.description || "-"}
                                </p>

                                <p>
                                    <b>Display Order:</b>{" "}
                                    {category.displayOrder}
                                </p>

                                <p>
                                    <b>Available:</b>{" "}
                                    {category.available
                                        ? "Yes"
                                        : "No"}
                                </p>

                                <p>
                                    <b>Organization:</b>{" "}
                                    {category.organizationModel
                                        ?.organizationName || "-"}
                                </p>

                                <p>
                                    <b>Branch:</b>{" "}
                                    {category.branchModel
                                        ?.branchName || "-"}
                                </p>

                            </div>

                            <div className="flex flex-wrap gap-2 mt-4">

                                {canView && (
                                    <button
                                        className="flex-1 bg-green-500 text-white py-2 rounded-lg"
                                        onClick={() => {
                                            setModalMode("view");
                                            setSelectedCategory(category);
                                            setShowModal(true);
                                        }}
                                    >
                                        View
                                    </button>
                                )}

                                {canUpdate && (
                                    <button
                                        className="flex-1 bg-blue-500 text-white py-2 rounded-lg"
                                        onClick={() => {
                                            setModalMode("edit");
                                            setSelectedCategory(category);
                                            setShowModal(true);
                                        }}
                                    >
                                        Edit
                                    </button>
                                )}

                                {category.isActive &&
                                    canDelete && (
                                        <button
                                            className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                                            onClick={() =>
                                                handleDelete(category.id)
                                            }
                                        >
                                            Delete
                                        </button>
                                    )}

                                {!category.isActive &&
                                    canRestore && (
                                        <button
                                            className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                                            onClick={() =>
                                                handleRestore(category.id)
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

            {/* =========================== MODAL =========================== */}

            <CategoryModelBox
                isOpen={showModal}
                onClose={() => setShowModal(false)}
                mode={modalMode}
                category={selectedCategory}
                onSuccess={loadCategories}
            />

        </div>
    );

}