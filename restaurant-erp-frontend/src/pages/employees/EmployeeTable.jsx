import { useEffect, useState } from "react";
import employeeService from "../../services/employeeService";
import EmployeeModelBox from "./EmployeeModelBox";
import FilterField from "../../components/FilterField";
import CustomSelect from "../../components/CustomSelect";
import {
  PlusIcon,
  EyeIcon,
  ArrowPathIcon,
  MagnifyingGlassIcon,
  FunnelIcon,
  ChevronUpIcon,
  ChevronDownIcon,
} from "@heroicons/react/24/outline";

import { PenLine, Trash, Users, Building2 } from "lucide-react";

const statusOptions = [
  { label: "All", value: "" },
  { label: "Active", value: "true" },
  { label: "Inactive", value: "false" },
];

const employmentOptions = [
  { label: "All", value: "" },
  { label: "Active", value: "ACTIVE" },
  { label: "On Leave", value: "ON_LEAVE" },
  { label: "Resigned", value: "RESIGNED" },
  { label: "Terminated", value: "TERMINATED" },
  { label: "Retired", value: "RETIRED" },
  { label: "Probation", value: "PROBATION" },
];

const orderOptions = [
  { label: "Descending", value: "DESC" },
  { label: "Ascending", value: "ASC" },
];

const pageSizeOptions = [
  { label: "10", value: 10 },
  { label: "25", value: 25 },
  { label: "50", value: 50 },
  { label: "100", value: 100 },
];

export default function EmployeeTable() {
  const [showFilters, setShowFilters] = useState(false);

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
    search: "",
    roleId: "",
    organizationId: "",
    branchId: "",
    employmentStatus: "",
    isActive: "",
  });

  const user = JSON.parse(localStorage.getItem("user"));

  const canCreate = user?.permissions?.includes("EMPLOYEE_CREATE");

  const canView = user?.permissions?.includes("EMPLOYEE_VIEW");

  const canUpdate = user?.permissions?.includes("EMPLOYEE_UPDATE");

  const canDelete = user?.permissions?.includes("EMPLOYEE_DELETE");

  const canRestore = user?.permissions?.includes("EMPLOYEE_REACTIVATE");

  const [confirmModal, setConfirmModal] = useState({
    open: false,
    title: "",
    message: "",
    action: null,
  });

  const statuses = [
    { name: "All", value: "" },
    { name: "Active", value: "true" },
    { name: "Inactive", value: "false" },
  ];

  const selectedStatus =
    statuses.find((status) => status.value === searchCriteria.isActive) ||
    statuses[0];

  const SortableHeader = ({ label, field }) => {
    const active = sortBy === field;

    useEffect(() => {
      const handleResize = () => {
        if (window.innerWidth >= 1024) {
          setShowFilters(false);
        }
      };

      window.addEventListener("resize", handleResize);

      return () => window.removeEventListener("resize", handleResize);
    }, []);

    return (
      <th
        onClick={() => handleSort(field)}
        className="cursor-pointer px-4 py-3 text-left hover:bg-gray-200"
      >
        <div className="flex items-center gap-1">
          <span>{label}</span>

          {active ? (
            direction === "ASC" ? (
              <ChevronUpIcon className="h-4 w-4 text-[#0d4039]" />
            ) : (
              <ChevronDownIcon className="h-4 w-4 text-[#0d4039]" />
            )
          ) : (
            <div className="flex flex-col leading-none opacity-40">
              <ChevronUpIcon className="h-3 w-3 -mb-1" />
              <ChevronDownIcon className="h-3 w-3" />
            </div>
          )}
        </div>
      </th>
    );
  };

  useEffect(() => {
    loadEmployees();
  }, [currentPage, pageSize, sortBy, direction]);

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

  const openDeleteModal = (id) => {
    setConfirmModal({
      open: true,
      title: "Delete Employee",
      message: "Are you sure you want to delete this employee?",
      action: async () => {
        try {
          await employeeService.delete(id);
          loadEmployees();
        } catch (error) {
          console.error(error);
          alert(error?.response?.data?.message || "Unable to delete employee.");
        } finally {
          setConfirmModal((prev) => ({
            ...prev,
            open: false,
          }));
        }
      },
    });
  };

  const openRestoreModal = (id) => {
    setConfirmModal({
      open: true,
      title: "Restore Employee",
      message: "Are you sure you want to restore this employee?",
      action: async () => {
        try {
          await employeeService.restore(id);
          loadEmployees();
        } catch (error) {
          console.error(error);
          alert(
            error?.response?.data?.message || "Unable to restore employee.",
          );
        } finally {
          setConfirmModal((prev) => ({
            ...prev,
            open: false,
          }));
        }
      },
    });
  };
  const handleSort = (field) => {
    setCurrentPage(0);

    if (sortBy === field) {
      setDirection((prev) => (prev === "ASC" ? "DESC" : "ASC"));
    } else {
      setSortBy(field);
      setDirection("ASC");
    }
  };

  const loadEmployees = async () => {
    try {
      setLoading(true);

      const payload = {
        search: searchCriteria.search || null,
        roleId: searchCriteria.roleId || null,
        organizationId: searchCriteria.organizationId || null,
        branchId: searchCriteria.branchId || null,
        employmentStatus: searchCriteria.employmentStatus || null,
        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
      };

      const response = await employeeService.search(
        payload,
        currentPage,
        pageSize,
        sortBy,
        direction,
      );

      setEmployees(response.content);
      setTotalPages(response.totalPages);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) =>
    status ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700";

  return (
    <div>
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        {/* =========================== HEADER =========================== */}
        <div className="flex justify-between items-center mb-5">
          <h2 className="text-2xl font-bold">Employees</h2>

          {canCreate && (
            <button
              onClick={() => {
                setModalMode("create");
                setSelectedEmployee(null);
                setShowModal(true);
              }}
              className="px-3 sm:px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium flex items-center justify-center gap-2"
            >
              <PlusIcon className="w-5 h-5" />
              <span className="hidden sm:inline">Add Employee</span>
            </button>
          )}
        </div>

        {/* ================= Search & Filters ================= */}

        <div>
          <div className="lg:hidden space-y-4">
            <div className="lg:hidden">
              <div className="relative">
                <input
                  type="text"
                  value={searchCriteria.search}
                  onChange={(e) =>
                    setSearchCriteria((prev) => ({
                      ...prev,
                      search: e.target.value,
                    }))
                  }
                  placeholder="Employee Name, Phone..."
                  className="w-full rounded-xl border border-gray-300 bg-white py-3 pl-11 pr-12 text-sm shadow-sm outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                />

                <MagnifyingGlassIcon className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />

                <button
                  onClick={() => setShowFilters(!showFilters)}
                  className="absolute right-2 top-1/2 -translate-y-1/2 rounded-lg p-2 hover:bg-gray-100"
                >
                  <FunnelIcon
                    className={`h-5 w-5 transition ${showFilters ? "rotate-180 text-blue-600" : "text-gray-500"
                      }`}
                  />
                </button>
              </div>
            </div>
          </div>

          {/* Mobile Filter Toggle */}
          <div
            className={`overflow-hidden transition-all duration-300 ease-in-out ${showFilters
              ? "max-h-[500px] opacity-100 mt-4"
              : "max-h-0 opacity-0"
              }`}
          >
            <div className=" shadow-sm space-y-4">
              {/* Hidden on mobile until expanded */}
              <div
                className={`
                        space-y-4
                        ${showFilters ? "block" : "hidden"}
                      `}
              >

                {/* Employment Status */}
                <FilterField
                  label="Employment"
                  className="w-full xl:flex-1 xl:min-w-[300px]"
                >
                  <CustomSelect
                    options={employmentOptions}
                    value={searchCriteria.employmentStatus}
                    onChange={(item) =>
                      setSearchCriteria((prev) => ({
                        ...prev,
                        employmentStatus: item.value,
                      }))
                    }
                  />
                </FilterField>

                {/* Status */}
                <FilterField
                  label="Status"
                  className="w-full xl:flex-1 xl:min-w-[300px]"
                >
                  <CustomSelect
                    options={statusOptions}
                    value={searchCriteria.isActive}
                    onChange={(item) =>
                      setSearchCriteria((prev) => ({
                        ...prev,
                        isActive: item.value,
                      }))
                    }
                  />
                </FilterField>

                {/* Rows */}
                <FilterField
                  label="Rows"
                  className="w-full xl:flex-1 xl:min-w-[70px]"
                >
                  <CustomSelect
                    options={pageSizeOptions}
                    value={pageSize}
                    onChange={(item) => {
                      setCurrentPage(0);
                      setPageSize(item.value);
                    }}
                  />
                </FilterField>

                <button
                  onClick={() => {
                    setSearchCriteria({
                      name: "",
                      isActive: "",
                    });

                    setSortBy("createdAt");
                    setDirection("DESC");
                    setPageSize(10);
                    setCurrentPage(0);
                  }}
                  className="w-full h-12 rounded-xl bg-gray-100 font-medium hover:bg-gray-200 flex items-center justify-center gap-2"
                >
                  <ArrowPathIcon className="h-5 w-5" />
                  Reset
                </button>
              </div>
            </div>
          </div>

          {/* Desktop Filter */}
          <div className="hidden lg:flex flex-wrap items-center gap-4">
            {/* Search */}
            <FilterField label="Search" className="flex-1 min-w-[300px]">
              <input
                type="text"
                value={searchCriteria.search}
                onChange={(e) =>
                  setSearchCriteria((prev) => ({
                    ...prev,
                    search: e.target.value,
                  }))
                }
                placeholder="Employee Name, Phone..."
                className="w-full border-0 bg-transparent text-sm focus:outline-none"
              />
            </FilterField>

            {/* Employment Status */}
            <FilterField
              label="Employment"
              className="w-full xl:flex-1 xl:min-w-[300px]"
            >
              <CustomSelect
                options={employmentOptions}
                value={searchCriteria.employmentStatus}
                onChange={(item) =>
                  setSearchCriteria((prev) => ({
                    ...prev,
                    employmentStatus: item.value,
                  }))
                }
              />
            </FilterField>

            {/* Status */}
            <FilterField
              label="Status"
              className="w-full xl:flex-1 xl:min-w-[300px]"
            >
              <CustomSelect
                options={statusOptions}
                value={searchCriteria.isActive}
                onChange={(item) =>
                  setSearchCriteria((prev) => ({
                    ...prev,
                    isActive: item.value,
                  }))
                }
              />
            </FilterField>

            {/* Order */}
            <FilterField
              label="Order"
              className="w-full xl:flex-1 xl:min-w-[300px]"
            >
              <CustomSelect
                options={orderOptions}
                value={direction}
                onChange={(item) => {
                  setCurrentPage(0);
                  setDirection(item.value);
                }}
              />
            </FilterField>

            {/* Rows */}
            <FilterField
              label="Rows"
              className="w-full xl:flex-1 xl:min-w-[70px]"
            >
              <CustomSelect
                options={pageSizeOptions}
                value={pageSize}
                onChange={(item) => {
                  setCurrentPage(0);
                  setPageSize(item.value);
                }}
              />
            </FilterField>

            {/* Reset */}
            <button
              onClick={() => {
                setSearchCriteria({
                  search: "",
                  roleId: "",
                  organizationId: "",
                  branchId: "",
                  employmentStatus: "",
                  isActive: "",
                });

                setSortBy("createdAt");
                setDirection("DESC");
                setCurrentPage(0);
                setPageSize(10);
              }}
              className="w-full md:w-auto h-12 flex items-center justify-center gap-2 rounded-xl bg-gray-100 px-6 font-bold transition hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300"
            >
              <ArrowPathIcon className="h-5 w-5" />
              <span>Reset</span>
            </button>
          </div>
        </div>
      </div>

      {/* =========================== DESKTOP TABLE =========================== */}
      <br />
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        <div className=" flex items-center justify-between mb-4 ">
          <p className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-blue-500">
              {employees.length}
            </span>{" "}
            Employees
          </p>
        </div>
        <div className="hidden lg:block overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
          <table className="w-full">
            <thead className="border-b border-gray-200 bg-gray-50">
              <tr>
                <SortableHeader label="Employee" field="fullName" />
                <SortableHeader label="Phone" field="phone" />
                <SortableHeader label="Role" field="role" />
                <SortableHeader label="Branch" field="branch" />
                <SortableHeader label="Status" field="isActive" />
                <th className="px-4 py-3 text-left">Action</th>
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
                  <tr key={employee.id} className="border-b hover:bg-gray-50">
                    {/* Employee */}
                    <td className="px-4 py-3">
                      <div className="font-medium text-gray-900">
                        {employee.fullName}
                      </div>

                      <div className="text-xs text-gray-500">
                        {employee.employeeCode}
                      </div>

                      <div className="text-xs text-gray-400">
                        {employee.userModel?.email || "-"}
                      </div>
                    </td>

                    {/* Phone */}
                    <td className="px-4 py-3">{employee.phone}</td>

                    {/* Role */}
                    <td className="px-4 py-3">
                      {employee.roleModel?.roleName || "-"}
                    </td>

                    {/* Branch */}
                    <td className="px-4 py-3">
                      {employee.branchModel?.branchName || "-"}
                    </td>

                    {/* Status */}
                    <td className="px-4 py-3">
                      <span
                        className={`rounded-full px-3 py-1 text-sm ${getStatusColor(
                          employee.isActive,
                        )}`}
                      >
                        {employee.isActive ? "ACTIVE" : "INACTIVE"}
                      </span>
                    </td>

                    {/* Actions */}
                    <td className="px-4 py-3">
                      {canView && (
                        <button
                          onClick={() => {
                            setModalMode("view");
                            setSelectedEmployee(employee);
                            setShowModal(true);
                          }}
                          className="mr-3 rounded-lg border p-1 text-blue-500 hover:bg-gray-50"
                        >
                          <EyeIcon className="h-5 w-5" title="View" />
                        </button>
                      )}

                      {canUpdate && (
                        <button
                          onClick={() => {
                            setModalMode("edit");
                            setSelectedEmployee(employee);
                            setShowModal(true);
                          }}
                          className="mr-3 rounded-lg border p-1 text-indigo-600 hover:bg-gray-50"
                        >
                          <PenLine className="h-5 w-5" title="Edit" />
                        </button>
                      )}

                      {employee.isActive && canDelete && (
                        <button
                          onClick={() => openDeleteModal(employee.id)}
                          className="mr-3 rounded-lg border p-1 text-red-600 hover:bg-gray-50"
                        >
                          <Trash className="h-5 w-5" title="Delete" />
                        </button>
                      )}

                      {!employee.isActive && canRestore && (
                        <button
                          onClick={() => openRestoreModal(employee.id)}
                          className="mr-3 rounded-lg border p-1 text-orange-600 hover:bg-gray-50"
                        >
                          <ArrowPathIcon className="h-5 w-5" title="Restore" />
                        </button>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Mobile List */}
        <div className="lg:hidden rounded-2xl border border-gray-200 bg-white overflow-hidden shadow-sm">
          {employees.length === 0 ? (
            <div className="p-8 text-center text-gray-500">
              No Employees Found
            </div>
          ) : (
            employees.map((employee) => (
              <div
                key={employee.id}
                className="border-b last:border-b-0 px-4 py-3 transition hover:bg-blue-50/40"
              >
                {/* Top */}
                <div className="flex items-start justify-between gap-3">
                  <div className="min-w-0 flex-1">
                    <div className="flex items-center gap-2">
                      <h3 className="truncate font-semibold text-gray-900">
                        {employee.fullName}
                      </h3>

                      <span
                        className={`rounded-full px-2 py-0.5 text-[10px] font-semibold ${employee.isActive
                          ? "bg-green-100 text-green-700"
                          : "bg-red-100 text-red-700"
                          }`}
                      >
                        {employee.isActive ? "ACTIVE" : "INACTIVE"}
                      </span>
                    </div>

                    <p className="mt-1 text-xs text-gray-500">
                      {employee.employeeCode}
                    </p>

                    <p className="text-xs text-gray-400">
                      {employee.userModel?.email || "-"}
                    </p>
                  </div>
                </div>

                {/* Middle */}
                <div className="mt-3 flex items-center justify-between">
                  <div className="space-y-1 text-xs text-gray-500">
                    <div className="flex items-center gap-2">
                      <Users className="h-3.5 w-3.5 text-blue-500" />
                      <span>{employee.roleModel?.roleName || "-"}</span>
                    </div>

                    <div className="flex items-center gap-2">
                      <Building2 className="h-3.5 w-3.5 text-blue-500" />
                      <span>{employee.branchModel?.branchName || "-"}</span>
                    </div>

                    <div>{employee.phone}</div>
                  </div>

                  {/* Actions */}
                  <div className="flex items-center">
                    {canView && (
                      <button
                        onClick={() => {
                          setModalMode("view");
                          setSelectedEmployee(employee);
                          setShowModal(true);
                        }}
                        className="rounded-lg p-2 text-blue-600 hover:bg-blue-100"
                      >
                        <EyeIcon className="h-5 w-5" />
                      </button>
                    )}

                    {canUpdate && (
                      <button
                        onClick={() => {
                          setModalMode("edit");
                          setSelectedEmployee(employee);
                          setShowModal(true);
                        }}
                        className="rounded-lg p-2 text-indigo-600 hover:bg-indigo-100"
                      >
                        <PenLine className="h-5 w-5" />
                      </button>
                    )}

                    {employee.isActive && canDelete && (
                      <button
                        onClick={() => openDeleteModal(employee.id)}
                        className="rounded-lg p-2 text-red-600 hover:bg-red-100"
                      >
                        <Trash className="h-5 w-5" />
                      </button>
                    )}

                    {!employee.isActive && canRestore && (
                      <button
                        onClick={() => openRestoreModal(employee.id)}
                        className="rounded-lg p-2 text-orange-600 hover:bg-orange-100"
                      >
                        <ArrowPathIcon className="h-5 w-5" />
                      </button>
                    )}
                  </div>
                </div>
              </div>
            ))
          )}
        </div>

        {/* ================= Pagination ================= */}

        <div className="mt-6 flex flex-col gap-4 border-t border-gray-200 pt-5 md:flex-row md:items-center md:justify-between">
          {/* Left */}

          <div className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-blue-500">
              {employees.length === 0 ? 0 : currentPage * pageSize + 1}
            </span>{" "}
            to{" "}
            <span className="font-semibold text-blue-500">
              {Math.min(
                (currentPage + 1) * pageSize,
                currentPage * pageSize + employees.length,
              )}
            </span>{" "}
            results
          </div>

          {/* Right */}

          <div className="flex items-center gap-2">
            {/* Previous */}

            <button
              disabled={currentPage === 0}
              onClick={() => setCurrentPage((prev) => prev - 1)}
              className={`rounded-xl border px-4 py-2 text-sm transition ${currentPage === 0
                ? "cursor-not-allowed border-gray-200 text-gray-400"
                : "border-gray-300 hover:bg-gray-50"
                }`}
            >
              Previous
            </button>

            {/* Pages */}

            {[...Array(totalPages)].map((_, index) => (
              <button
                key={index}
                onClick={() => setCurrentPage(index)}
                className={`h-10 w-10 rounded-xl text-sm font-medium transition ${currentPage === index
                  ? "bg-blue-600 text-white shadow-sm"
                  : "border border-gray-200 bg-white hover:bg-gray-50"
                  }`}
              >
                {index + 1}
              </button>
            ))}

            {/* Next */}

            <button
              disabled={currentPage === totalPages - 1 || totalPages === 0}
              onClick={() => setCurrentPage((prev) => prev + 1)}
              className={`rounded-xl border px-4 py-2 text-sm transition ${currentPage === totalPages - 1 || totalPages === 0
                ? "cursor-not-allowed border-gray-200 text-gray-400"
                : "border-gray-300 hover:bg-gray-50"
                }`}
            >
              Next
            </button>
          </div>
        </div>

        {confirmModal.open && (
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
            <div className="w-[90%] max-w-md rounded-xl bg-white p-6 shadow-lg">
              <h2 className="text-xl font-bold text-gray-800">
                {confirmModal.title}
              </h2>

              <p className="mt-3 text-gray-600">{confirmModal.message}</p>

              <div className="mt-6 flex justify-end gap-3">
                <button
                  onClick={() =>
                    setConfirmModal((prev) => ({
                      ...prev,
                      open: false,
                    }))
                  }
                  className="rounded-lg border border-gray-300 px-5 py-2 hover:bg-gray-100"
                >
                  Cancel
                </button>

                <button
                  onClick={confirmModal.action}
                  className="rounded-lg bg-blue-600 px-5 py-2 text-white hover:bg-blue-700"
                >
                  Confirm
                </button>
              </div>
            </div>
          </div>
        )}

        {/* MODAL */}
        <EmployeeModelBox
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          mode={modalMode}
          employee={selectedEmployee}
          onSuccess={loadEmployees}
        />
      </div>
    </div>
  );
}
