import { useEffect, useState } from "react";
import employeeService from "../../services/employeeService";

import EmployeeModelBox from "./EmployeeModelBox";

export default function EmployeeTable() {

    const [employees, setEmployees] = useState([]);

    const [loading, setLoading] = useState(false);

    const [showModal, setShowModal] = useState(false);

    const [modalMode, setModalMode] = useState("create");

    const [selectedEmployee, setSelectedEmployee] = useState(null);

    const [currentPage, setCurrentPage] = useState(0);

    const [totalPages, setTotalPages] = useState(0);

    const [pageSize, setPageSize] = useState(10);

    const [sortBy, setSortBy] = useState("createdAt");

    const [direction, setDirection] = useState("DESC");

    const [searchCriteria, setSearchCriteria] = useState({
        employeeCode: "",
        fullName: "",
        email: "",
        phone: "",
        cnic: "",
        designation: "",
        organization: "",
        branch: "",
        role: "",
        employmentStatus: "",
        isActive: ""
    });

    const user = JSON.parse(localStorage.getItem("user"));

    const canCreate = user?.permissions?.includes("EMPLOYEE_CREATE");

    const canView = user?.permissions?.includes("EMPLOYEE_VIEW");

    const canUpdate = user?.permissions?.includes("EMPLOYEE_UPDATE");

    const canDelete = user?.permissions?.includes("EMPLOYEE_DELETE");

    const canRestore = user?.permissions?.includes("EMPLOYEE_REACTIVATE");

    const loadEmployees = async () => {
        try {
            setLoading(true);
            const payload = {
                employeeCode: searchCriteria.employeeCode || null,
                fullName: searchCriteria.fullName || null,
                email: searchCriteria.email || null,
                phone: searchCriteria.phone || null,
                cnic: searchCriteria.cnic || null,
                designation: searchCriteria.designation || null,
                organization: searchCriteria.organization || null,
                branch: searchCriteria.branch || null,
                role: searchCriteria.role || null,
                employmentStatus: searchCriteria.employmentStatus || null,
                isActive: searchCriteria.isActive === "" ? null : searchCriteria.isActive === "true"
            };

            const response = await employeeService.search(payload, currentPage, pageSize, sortBy, direction);

            console.log(response);

            setEmployees(response.content);
            setTotalPages(response.totalPages);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadEmployees();
    }, [
        currentPage,
        pageSize,
        sortBy,
        direction
    ]);

    useEffect(() => {
    const timer = setTimeout(() => {
        setCurrentPage(0);
        loadEmployees();
    }, 300);

    return () => clearTimeout(timer);
}, [searchCriteria, sortBy, direction, pageSize]);

    useEffect(() => {
        const eventSource = employeeService.stream();
        eventSource.onmessage = () => {
            loadEmployees();
        };

        eventSource.onerror = () => {
            eventSource.close();
        };

        return () => eventSource.close();
    }, []);

    const handleDelete = async (id) => {
        if (!window.confirm("Delete this employee?")) {
            return;
        }
        try {
            await employeeService.delete(id);
            alert("Employee Deleted Successfully");
            loadEmployees();
        } catch (error) {
            console.error(error);
            alert(error?.response?.data?.message || "Unable to delete employee.");
        }
    };

    const handleRestore = async (id) => {
        if (!window.confirm("Restore this employee?")) {
            return;
        }
        try {
            await employeeService.restore(id);
            alert("Employee Restored Successfully");
            loadEmployees();
        } catch (error) {
            console.error(error);
            alert(error?.response?.data?.message || "Unable to restore employee.");
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
                    Employees
                </h2>

                {canCreate && (
                    <button
                        onClick={() => {
                            setModalMode("create");
                            setSelectedEmployee(null);
                            setShowModal(true);
                        }}
                        className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
                    >
                        + Add Employee
                    </button>
                )}
            </div>

            {/* =========================== SEARCH PANEL =========================== */}

            <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5">

        <input
            type="text"
            placeholder="Employee Code"
            value={searchCriteria.employeeCode}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    employeeCode: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        <input
            type="text"
            placeholder="Full Name"
            value={searchCriteria.fullName}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    fullName: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        <input
            type="text"
            placeholder="Email"
            value={searchCriteria.email}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    email: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        <input
            type="text"
            placeholder="Phone"
            value={searchCriteria.phone}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    phone: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        <input
            type="text"
            placeholder="CNIC"
            value={searchCriteria.cnic}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    cnic: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        <input
            type="text"
            placeholder="Designation"
            value={searchCriteria.designation}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    designation: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

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

        <input
            type="text"
            placeholder="Role"
            value={searchCriteria.role}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    role: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        />

        <select
            value={searchCriteria.employmentStatus}
            onChange={(e) =>
                setSearchCriteria((prev) => ({
                    ...prev,
                    employmentStatus: e.target.value,
                }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        >
            <option value="">Employment Status</option>
            <option value="ACTIVE">ACTIVE</option>
            <option value="ON_LEAVE">ON LEAVE</option>
            <option value="RESIGNED">RESIGNED</option>
            <option value="TERMINATED">TERMINATED</option>
        </select>

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

        <select
            value={sortBy}
            onChange={(e) => {
                setCurrentPage(0);
                setSortBy(e.target.value);
            }}
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
        >
            <option value="createdAt">Created</option>
            <option value="employeeCode">Employee Code</option>
            <option value="fullName">Full Name</option>
        </select>

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

                                <th className="py-3">Employee Name</th>

                                <th className="py-3">Phone</th>

                                <th className="py-3">Role</th>

                                <th className="py-3">Organization</th>

                                <th className="py-3">Branch</th>

                                <th className="py-3">Status</th>

                                <th className="py-3">Action</th>

                            </tr>
                        </thead>

                        <tbody>
                            {employees.length === 0 ? (
                                <tr>
                                    <td colSpan="9" className="py-10 text-center text-gray-500">
                                        No Employees Found
                                    </td>
                                </tr>
                            ) : (
                                employees.map((employee) => (
                                    <tr
                                        key={employee.id}
                                        className="border-b hover:bg-gray-50"
                                    >
                                        <td className="py-3">
                                            {employee.employeeCode}
                                        </td>

                                        <td className="py-3">
                                            <div className="font-medium">
                                                {employee.fullName}
                                            </div>

                                            <div className="text-xs text-gray-500">
                                                {employee.userModel?.email || "-"}
                                            </div>
                                        </td>

                                        <td className="py-3">
                                            {employee.phone}
                                        </td>

                                        <td className="py-3">
                                            {employee.roleModel?.roleName || "-"}
                                        </td>

                                        <td className="py-3">
                                            {employee.organizationModel?.organizationName || "-"}
                                        </td>

                                        <td className="py-3">
                                            {employee.branchModel?.branchName || "-"}
                                        </td>

                                        <td className="py-3">
                                            <span
                                                className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                                                    employee.isActive
                                                )}`}
                                            >
                                                {employee.isActive ? "ACTIVE" : "INACTIVE"}
                                            </span>
                                        </td>

                                        <td className="py-3 space-x-3">

                                            {canView && (
                                                <button
                                                    className="text-green-600"
                                                    onClick={() => {
                                                        setModalMode("view");
                                                        setSelectedEmployee(employee);
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
                                                        setSelectedEmployee(employee);
                                                        setShowModal(true);
                                                    }}
                                                >
                                                    Edit
                                                </button>
                                            )}

                                            {employee.isActive &&
                                                canDelete && (
                                                    <button
                                                        className="text-red-600"
                                                        onClick={() => handleDelete(employee.id)}
                                                    >
                                                        Delete
                                                    </button>
                                                )}

                                            {!employee.isActive &&
                                                canRestore && (
                                                    <button
                                                        className="text-orange-600"
                                                        onClick={() => handleRestore(employee.id)}
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

            {/* Mobile View, Pagination & Modal will be added in Part 2 */}
            {/* MOBILE */}
            <div className="md:hidden space-y-4">
                {employees.map((employee) => (
                    <div
                        key={employee.id}
                        className="border rounded-xl p-4 bg-white"
                    >
                        <div className="flex justify-between items-start">
                            <h3 className="font-bold">
                                {employee.fullName}
                            </h3>

                            <span
                                className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                                    employee.isActive
                                )}`}
                            >
                                {employee.isActive ? "ACTIVE" : "INACTIVE"}
                            </span>
                        </div>

                        <div className="mt-3 space-y-1 text-sm">
                            <p>
                                <b>Employee Code:</b>{" "}
                                {employee.employeeCode}
                            </p>

                            <p>
                                <b>Phone:</b>{" "}
                                {employee.phone}
                            </p>

                            <p>
                                <b>Role:</b>{" "}
                                {employee.roleModel?.roleName || "N/A"}
                            </p>

                            <p>
                                <b>Organization:</b>{" "}
                                {employee.organizationModel?.organizationName || "N/A"}
                            </p>

                            <p>
                                <b>Branch:</b>{" "}
                                {employee.branchModel?.branchName || "N/A"}
                            </p>
                        </div>

                        <div className="flex gap-2 mt-4">

                            {canView && (
                                <button
                                    className="flex-1 bg-green-500 text-white py-2 rounded-lg"
                                    onClick={() => {
                                        setModalMode("view");
                                        setSelectedEmployee(employee);
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
                                        setSelectedEmployee(employee);
                                        setShowModal(true);
                                    }}
                                >
                                    Edit
                                </button>
                            )}

                            {employee.isActive &&
                                canDelete && (
                                    <button
                                        className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                                        onClick={() => handleDelete(employee.id)}
                                    >
                                        Delete
                                    </button>
                                )}

                            {!employee.isActive &&
                                canRestore && (
                                    <button
                                        className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                                        onClick={() => handleRestore(employee.id)}
                                    >
                                        Restore
                                    </button>
                                )}

                        </div>
                    </div>
                ))}
            </div>

            {/* PAGINATION */}
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

            {/* MODAL */}
            <EmployeeModelBox
                isOpen={showModal}
                onClose={() => setShowModal(false)}
                mode={modalMode}
                employee={selectedEmployee}
                onSuccess={loadEmployees}
            />

        </div>
    );

}