import { useEffect, useState } from "react";
import tableManagementService from "../../services/tableManagementService";

import TableManagementModelBox from "./TableManagementModelBox";

export default function TableManagementTable() {

    const [tables, setTables] = useState([]);

    const [loading, setLoading] = useState(false);

    const [showModal, setShowModal] = useState(false);

    const [modalMode, setModalMode] = useState("create");

    const [selectedTable, setSelectedTable] = useState(null);

    const [currentPage, setCurrentPage] = useState(0);

    const [totalPages, setTotalPages] = useState(0);

    const [pageSize, setPageSize] = useState(10);

    const [sortBy, setSortBy] = useState("createdAt");

    const [direction, setDirection] = useState("DESC");

    const [searchCriteria, setSearchCriteria] = useState({
        tableNumber: "",
        tableName: "",
        capacity: "",
        qrToken: "",
        organization: "",
        branch: "",
        isActive: ""
    });

    const user = JSON.parse(localStorage.getItem("user"));

    const canCreate = user?.permissions?.includes("TABLE_CREATE");

    const canView = user?.permissions?.includes("TABLE_VIEW");

    const canUpdate = user?.permissions?.includes("TABLE_UPDATE");

    const canDelete = user?.permissions?.includes("TABLE_DELETE");

    const canRestore = user?.permissions?.includes("TABLE_REACTIVATE");

    const loadTables = async () => {

        try {

            setLoading(true);

            const payload = {

                tableNumber: searchCriteria.tableNumber || null,

                tableName: searchCriteria.tableName || null,

                capacity:
                    searchCriteria.capacity === ""
                        ? null
                        : Number(searchCriteria.capacity),

                qrToken: searchCriteria.qrToken || null,

                organization: searchCriteria.organization || null,

                branch: searchCriteria.branch || null,

                isActive:
                    searchCriteria.isActive === ""
                        ? null
                        : searchCriteria.isActive === "true"

            };

            const response = await tableManagementService.search(
                payload,
                currentPage,
                pageSize,
                sortBy,
                direction
            );

            console.log(response);

            setTables(response.content);

            setTotalPages(response.totalPages);

        } catch (error) {

            console.error(error);

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => {

        loadTables();

    }, [
        currentPage,
        pageSize,
        sortBy,
        direction
    ]);

    useEffect(() => {
    const timer = setTimeout(() => {
        setCurrentPage(0);
        loadTables();
    }, 300);

    return () => clearTimeout(timer);
}, [searchCriteria, sortBy, direction, pageSize]);

    useEffect(() => {

        const eventSource = tableManagementService.stream();

        eventSource.onmessage = () => {

            loadTables();

        };

        eventSource.onerror = () => {

            eventSource.close();

        };

        return () => eventSource.close();

    }, []);

    const handleDelete = async (id) => {

        if (!window.confirm("Delete this table?")) {

            return;

        }

        try {

            await tableManagementService.delete(id);

            alert("Table Deleted Successfully");

            loadTables();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to delete table."
            );

        }

    };

    const handleRestore = async (id) => {

        if (!window.confirm("Restore this table?")) {

            return;

        }

        try {

            await tableManagementService.restore(id);

            alert("Table Restored Successfully");

            loadTables();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to restore table."
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
                    Tables
                </h2>

                {canCreate && (
                    <button
                        onClick={() => {
                            setModalMode("create");
                            setSelectedTable(null);
                            setShowModal(true);
                        }}
                        className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
                    >
                        + Add Table
                    </button>
                )}
            </div>

            {/* =========================== SEARCH PANEL =========================== */}

            <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">

    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5">

        {/* Table Number */}

        <input
            type="text"
            placeholder="Table Number"
            value={searchCriteria.tableNumber}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    tableNumber: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        {/* Table Name */}

        <input
            type="text"
            placeholder="Table Name"
            value={searchCriteria.tableName}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    tableName: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        {/* Capacity */}

        <input
            type="number"
            placeholder="Capacity"
            value={searchCriteria.capacity}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    capacity: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        {/* QR Token */}

        <input
            type="text"
            placeholder="QR Token"
            value={searchCriteria.qrToken}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    qrToken: e.target.value,
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
            <option value="tableNumber">Table Number</option>
            <option value="tableName">Table Name</option>
            <option value="capacity">Capacity</option>
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

                                <th className="py-3">Table No.</th>

                                <th className="py-3">Table Name</th>

                                <th className="py-3">Capacity</th>

                                <th className="py-3">QR Token</th>

                                <th className="py-3">Organization</th>

                                <th className="py-3">Branch</th>

                                <th className="py-3">Status</th>

                                <th className="py-3">Action</th>

                            </tr>
                        </thead>

                        <tbody>

                            {tables.length === 0 ? (

                                <tr>

                                    <td
                                        colSpan="8"
                                        className="py-10 text-center text-gray-500"
                                    >
                                        No Tables Found
                                    </td>

                                </tr>

                            ) : (

                                tables.map((table) => (

                                    <tr
                                        key={table.id}
                                        className="border-b hover:bg-gray-50"
                                    >

                                        <td className="py-3">
                                            {table.tableNumber}
                                        </td>

                                        <td className="py-3 font-medium">
                                            {table.tableName}
                                        </td>

                                        <td className="py-3">
                                            {table.capacity}
                                        </td>

                                        <td className="py-3">
                                            {table.qrToken || "-"}
                                        </td>

                                        <td className="py-3">
                                            {table.organizationModel?.organizationName || "-"}
                                        </td>

                                        <td className="py-3">
                                            {table.branchModel?.branchName || "-"}
                                        </td>

                                        <td className="py-3">

                                            <span
                                                className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                                                    table.isActive
                                                )}`}
                                            >
                                                {table.isActive
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
                                                        setSelectedTable(table);
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
                                                        setSelectedTable(table);
                                                        setShowModal(true);
                                                    }}
                                                >
                                                    Edit
                                                </button>
                                            )}

                                            {table.isActive &&
                                                canDelete && (
                                                    <button
                                                        className="text-red-600 hover:underline"
                                                        onClick={() =>
                                                            handleDelete(table.id)
                                                        }
                                                    >
                                                        Delete
                                                    </button>
                                                )}

                                            {!table.isActive &&
                                                canRestore && (
                                                    <button
                                                        className="text-orange-600 hover:underline"
                                                        onClick={() =>
                                                            handleRestore(table.id)
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

                ) : tables.length === 0 ? (

                    <div className="text-center py-10 text-gray-500">
                        No Tables Found
                    </div>

                ) : (

                    tables.map((table) => (

                        <div
                            key={table.id}
                            className="border rounded-xl p-4 shadow-sm bg-white"
                        >

                            <div className="flex justify-between items-start">

                                <div>

                                    <h3 className="font-bold text-lg">
                                        {table.tableName}
                                    </h3>

                                    <p className="text-sm text-gray-500">
                                        {table.tableNumber}
                                    </p>

                                </div>

                                <span
                                    className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                                        table.isActive
                                    )}`}
                                >
                                    {table.isActive
                                        ? "ACTIVE"
                                        : "INACTIVE"}
                                </span>

                            </div>

                            <div className="mt-4 space-y-2 text-sm">

                                <p>
                                    <b>Capacity:</b>{" "}
                                    {table.capacity}
                                </p>

                                <p>
                                    <b>QR Token:</b>{" "}
                                    {table.qrToken || "-"}
                                </p>

                                <p>
                                    <b>Organization:</b>{" "}
                                    {table.organizationModel?.organizationName || "-"}
                                </p>

                                <p>
                                    <b>Branch:</b>{" "}
                                    {table.branchModel?.branchName || "-"}
                                </p>

                            </div>

                            <div className="flex flex-wrap gap-2 mt-5">

                                {canView && (
                                    <button
                                        className="flex-1 bg-green-600 text-white py-2 rounded-lg"
                                        onClick={() => {
                                            setModalMode("view");
                                            setSelectedTable(table);
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
                                            setSelectedTable(table);
                                            setShowModal(true);
                                        }}
                                    >
                                        Edit
                                    </button>
                                )}

                                {table.isActive &&
                                    canDelete && (
                                        <button
                                            className="flex-1 bg-red-600 text-white py-2 rounded-lg"
                                            onClick={() =>
                                                handleDelete(table.id)
                                            }
                                        >
                                            Delete
                                        </button>
                                    )}

                                {!table.isActive &&
                                    canRestore && (
                                        <button
                                            className="flex-1 bg-orange-600 text-white py-2 rounded-lg"
                                            onClick={() =>
                                                handleRestore(table.id)
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

            {/* =========================== TABLE MANAGEMENT MODAL =========================== */}

            <TableManagementModelBox
                isOpen={showModal}
                onClose={() => setShowModal(false)}
                mode={modalMode}
                tableData={selectedTable}
                onSuccess={loadTables}
            />

        </div>
    );

}