import { useEffect, useState } from "react";
import axiosClient from "../../../api/axiosClient";
import PermissionModalBox from "./PermissionModalBox";
import FilterField from "../../../components/FilterField";
import CustomSelect from "../../../components/CustomSelect";
import { Listbox } from "@headlessui/react";
import { toast } from "react-toastify";
import { PenLine } from "lucide-react";
import {
  PlusIcon,
  EyeIcon,
  TrashIcon,
  MagnifyingGlassIcon,
  FunnelIcon,
  ArrowPathIcon,
  ChevronUpIcon,
  ChevronDownIcon,
  ChevronUpDownIcon,
  CheckIcon,
} from "@heroicons/react/24/outline";
import { Trash, ScanEye } from "lucide-react";
const statusOptions = [
  { label: "All", value: "" },
  { label: "Active", value: "true" },
  { label: "Inactive", value: "false" },
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

const API_URL = import.meta.env.VITE_API_URL;

export default function PermissionTable() {
  const [showFilters, setShowFilters] = useState(false);
  const [permissions, setPermissions] = useState([]);
  const [loading, setLoading] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");
  const [selectedPermission, setSelectedPermission] = useState(null);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [pageSize, setPageSize] = useState(10);
  const [sortBy, setSortBy] = useState("createdAt");
  const [direction, setDirection] = useState("DESC");

  const [searchCriteria, setSearchCriteria] = useState({

    searchInput: "",
    isActive: "",

  });

  const [confirmModal, setConfirmModal] = useState({
    open: false,
    title: "",
    message: "",
    action: null,
  });

  const user = JSON.parse(localStorage.getItem("user"));

  const canCreate = user?.permissions?.includes("PERMISSION_CREATE");

  const canView = user?.permissions?.includes("PERMISSION_VIEW");

  const canUpdate = user?.permissions?.includes("PERMISSION_UPDATE");

  const canDelete = user?.permissions?.includes("PERMISSION_DELETE");

  const canRestore = user?.permissions?.includes("PERMISSION_REACTIVATE");

  const handleSort = (field) => {
    setCurrentPage(0);

    if (sortBy === field) {
      setDirection((prev) => (prev === "ASC" ? "DESC" : "ASC"));
    } else {
      setSortBy(field);
      setDirection("ASC");
    }
  };

  const SortableHeader = ({ label, field }) => {
    const active = sortBy === field;

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

  const openDeleteModal = (id) => {
    setConfirmModal({
      open: true,
      title: "Delete Permission",
      message: "Are you sure you want to delete this permission?",
      action: async () => {
        try {
          await axiosClient.delete(`/permission/${id}`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to delete permission",
          );
        } finally {
          setConfirmModal((prev) => ({ ...prev, open: false }));
        }
      },
    });
  };

  const openRestoreModal = (id) => {
    setConfirmModal({
      open: true,
      title: "Restore Permission",
      message: "Are you sure you want to restore this permission?",
      action: async () => {
        try {
          await axiosClient.patch(`/permission/${id}/restore`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to restore permission",
          );
        } finally {
          setConfirmModal((prev) => ({ ...prev, open: false }));
        }
      },
    });
  };

  // ===== LOAD =====
  const loadPermissions = async () => {
    try {
      setLoading(true);

      const payload = {
        searchInput: searchCriteria.searchInput || null,
        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
      };

      const response = await axiosClient.post(
        `/permission/search?page=${currentPage}&size=${pageSize}&sortBy=${sortBy}&direction=${direction}`,
        payload,
      );

      const page = response.data.data;

      setPermissions(page.content || []);
      setTotalPages(page.totalPages || 0);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  // ===== INIT =====
  useEffect(() => {
    loadPermissions();
  }, [currentPage, pageSize, sortBy, direction]);

  useEffect(() => {
    const timer = setTimeout(() => {
      setCurrentPage(0);
      loadPermissions();
    }, 300);

    return () => clearTimeout(timer);
  }, [searchCriteria, sortBy, direction, pageSize]);

  // ===== SSE =====
  useEffect(() => {
    const eventSource = new EventSource(`${API_URL}/permission/stream`);

    eventSource.addEventListener("permission-created", () => {
      loadPermissions();
      toast.success("A new permission was created.");
    });

    eventSource.addEventListener("permission-updated", () => {
      loadPermissions();
      toast.success("A permission was updated.");
    });

    eventSource.addEventListener("permission-deleted", async (event) => {
      loadPermissions();
      toast.success("A permission was deleted.");
    });

    eventSource.addEventListener("permission-restored", () => {
      loadPermissions();
      toast.success("A permission was restored.");
    });

    return () => eventSource.close();
  }, []);

  const getStatusColor = (status) =>
    status
      ? "bg-green-100 text-green-700"
      : "bg-red-100 text-red-700";

  const pages = Array.from({ length: totalPages }, (_, i) => i);

  return (
    <div>
      {/* HEADER */}
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        {/* Header */}
        <div className="flex justify-between items-center mb-5">
          <h2 className="text-2xl font-bold">Permissions</h2>

          {canCreate && (
            <button
              onClick={() => {
                setModalMode("create");
                setSelectedPermission(null);
                setShowModal(true);
              }}
              className="flex items-center justify-center gap-2 rounded-lg bg-[#0d4039] px-3 py-2 font-medium text-white sm:px-6"
            >
              <PlusIcon className="h-5 w-5" />
              <span className="hidden sm:inline">
                Add Permission
              </span>
            </button>)}
        </div>

        {/* ================= Search & Filters ================= */}

        <div>
          {/* Mobile Search */}
          <div className="lg:hidden space-y-4">
            <div className="relative">
              <input
                type="text"
                value={searchCriteria.searchInput}
                onChange={(e) =>
                  setSearchCriteria((prev) => ({
                    ...prev,
                    searchInput: e.target.value,
                  }))
                }
                placeholder="Permission Name..."
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

          {/* Mobile Filters */}
          <div
            className={`lg:hidden overflow-hidden transition-all duration-300 ease-in-out ${showFilters
              ? "max-h-[500px] opacity-100 mt-4"
              : "max-h-0 opacity-0"
              }`}
          >
            <div className="space-y-4">
              {showFilters && (
                <>
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
                    className="w-full xl:flex-1 xl:min-w-[100px]"
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
                        searchInput: "",
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
                </>
              )}
            </div>
          </div>

          {/* Desktop Filters */}
          <div className="hidden lg:flex flex-wrap items-center gap-4">
            {/* Search */}
            <FilterField label="Search" className="flex-1 min-w-[300px]">
              <input
                type="text"
                value={searchCriteria.searchInput}
                onChange={(e) =>
                  setSearchCriteria((prev) => ({
                    ...prev,
                    searchInput: e.target.value,
                  }))
                }
                placeholder="Permission Name..."
                className="w-full border-0 bg-transparent text-sm focus:outline-none"
              />
            </FilterField>

            {/* Status */}
            <FilterField
              label="Status"
              className="w-full xl:flex-1 xl:min-w-[250px]"
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
              className="w-full xl:flex-1 xl:min-w-[250px]"
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
              className="w-full xl:flex-1 xl:min-w-[100px]"
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
                  searchInput: "",
                  isActive: "",
                });

                setSortBy("createdAt");
                setDirection("DESC");
                setPageSize(10);
                setCurrentPage(0);
              }}
              className="w-full md:w-auto h-12 flex items-center justify-center gap-2 rounded-xl bg-gray-100 px-6 font-bold transition hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300"
            >
              <ArrowPathIcon className="h-5 w-5" />
              <span>Reset</span>
            </button>
          </div>
        </div>
      </div>

      {/* TABLE */}
      <br />
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        {/* Summary */}
        <div className="flex items-center justify-between mb-4">
          <p className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-blue-600">
              {permissions.length}
            </span>{" "}
            Permissions
          </p>
        </div>

        {/* Desktop Table */}
        <div className="hidden lg:block overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
          {loading ? (
            <div className="py-10 text-center text-gray-500">
              Loading...
            </div>
          ) : (
            <table className="min-w-full">
              <thead className="border-b border-gray-200 bg-gray-50">
                <tr className="bg-gray-100">
                  <SortableHeader label="Code" field="code" />

                  <SortableHeader label="Permission Name" field="name" />

                  <SortableHeader label="Module" field="module" />

                  <SortableHeader label="Status" field="isActive" />

                  <th className="rounded-tr-xl px-4 py-3 text-left">
                    Action
                  </th>
                </tr>
              </thead>

              <tbody>
                {permissions.map((permission) => (
                  <tr
                    key={permission.id}
                    className="border-b border-gray-100 transition-colors hover:bg-gray-50"
                  >
                    <td className="px-4 py-3 font-medium text-gray-800">
                      {permission.code}
                    </td>

                    <td className="px-4 py-3 text-gray-600">
                      {permission.name}
                    </td>

                    <td className="px-4 py-3 text-gray-600">
                      {permission.module}
                    </td>

                    <td className="px-4 py-3">
                      <span
                        className={`rounded-full px-3 py-1 text-sm font-medium ${getStatusColor(
                          permission.isActive
                        )}`}
                      >
                        {permission.isActive
                          ? "ACTIVE"
                          : "INACTIVE"}
                      </span>
                    </td>

                    <td className="px-4 py-3">
                      {canView && (
                        <button
                          onClick={() => {
                            setModalMode("view");
                            setSelectedPermission(permission);
                            setShowModal(true);
                          }}
                          className="mr-3 rounded-lg border p-1 text-green-600 hover:bg-gray-50"
                        >
                          <EyeIcon
                            className="h-5 w-5"
                            title="View"
                          />
                        </button>)}

                      {canUpdate && (
                        <button
                          onClick={() => {
                            setModalMode("edit");
                            setSelectedPermission(permission);
                            setShowModal(true);
                          }}
                          className="mr-3 rounded-lg border p-1 text-blue-600 hover:bg-gray-50"
                        >
                          <PenLine
                            className="h-5 w-5"
                            title="Edit"
                          />
                        </button>)}

                      {permission.isActive && canDelete && (
                        <button
                          onClick={() => openDeleteModal(permission.id)}
                          className="mr-3 rounded-lg border p-1 text-red-600 hover:bg-gray-50"
                        >
                          <Trash
                            className="h-5 w-5"
                            title="Delete"
                          />
                        </button>
                      )}
                      {canRestore && !permission.isActive && (
                        <button
                          onClick={() => openRestoreModal(permission.id)}
                          className="mr-3 rounded-lg border p-1 text-orange-600 hover:bg-gray-50"
                        >
                          <ArrowPathIcon
                            className="h-5 w-5"
                            title="Restore"
                          />
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>


        {/* MOBILE */}
        <div className="lg:hidden rounded-2xl border border-gray-200 bg-white overflow-hidden shadow-sm">
          {loading ? (
            <div className="py-10 text-center">Loading...</div>
          ) : (
            permissions.map((permission) => (
              <div
                key={permission.id}
                className="px-4 py-3 border-b last:border-b-0 hover:bg-blue-50/40 transition"
              >
                {/* Header */}
                <div className="flex items-start justify-between gap-3">
                  <div className="min-w-0 flex-1">
                    <div className="flex items-center gap-2">
                      <h3 className="truncate font-semibold text-gray-900">
                        {permission.name}
                      </h3>

                      <span
                        className={`rounded-full px-2 py-0.5 text-[10px] font-semibold ${permission.isActive
                          ? "bg-green-100 text-green-700"
                          : "bg-red-100 text-red-700"
                          }`}
                      >
                        {permission.isActive ? "ACTIVE" : "INACTIVE"}
                      </span>
                    </div>

                    <p className="mt-1 text-xs text-gray-500">
                      {permission.module || "N/A"}
                    </p>
                  </div>
                </div>

                {/* Details */}
                <div className="mt-3 space-y-1 text-sm">
                  <p>
                    <b>Code:</b> {permission.code}
                  </p>

                  <p>
                    <b>Module:</b> {permission.module || "N/A"}
                  </p>
                </div>

                {/* Actions */}
                <div className="mt-3 flex items-center justify-end gap-1">
                  {canView && (
                    <button
                      onClick={() => {
                        setModalMode("view");
                        setSelectedPermission(permission);
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
                        setSelectedPermission(permission);
                        setShowModal(true);
                      }}
                      className="rounded-lg p-2 text-indigo-600 hover:bg-indigo-100"
                    >
                      <PenLine className="h-5 w-5" />
                    </button>
                  )}

                  {permission.isActive && canDelete && (
                    <button
                      onClick={() => openDeleteModal(permission.id)}
                      className="rounded-lg p-2 text-red-600 hover:bg-red-100"
                    >
                      <Trash className="h-5 w-5" />
                    </button>
                  )}

                  {!permission.isActive && canRestore && (
                    <button
                      onClick={() => openRestoreModal(permission.id)}
                      className="rounded-lg p-2 text-orange-600 hover:bg-orange-100"
                    >
                      <ArrowPathIcon className="h-5 w-5" />
                    </button>
                  )}
                </div>
              </div>
            ))
          )}
        </div>


        {/* PAGINATION */}
        <div className="mt-6 flex flex-col gap-4 border-t border-gray-200 pt-5 md:flex-row md:items-center md:justify-between">

          {/* Left */}
          <div className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-blue-600">
              {permissions.length === 0 ? 0 : currentPage * pageSize + 1}
            </span>{" "}
            to{" "}
            <span className="font-semibold text-blue-600">
              {Math.min(
                (currentPage + 1) * pageSize,
                currentPage * pageSize + permissions.length
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

            {/* Page Numbers */}
            {pages.map((page) => (
              <button
                key={page}
                onClick={() => setCurrentPage(page)}
                className={`h-10 w-10 rounded-xl text-sm font-medium transition ${currentPage === page
                  ? "bg-[#0d4039] text-white shadow-sm"
                  : "border border-gray-200 bg-white hover:bg-gray-50"
                  }`}
              >
                {page + 1}
              </button>
            ))}

            {/* Next */}
            <button
              disabled={
                currentPage === pages.length - 1 || pages.length === 0
              }
              onClick={() => setCurrentPage((prev) => prev + 1)}
              className={`rounded-xl border px-4 py-2 text-sm transition ${currentPage === pages.length - 1 || pages.length === 0
                ? "cursor-not-allowed border-gray-200 text-gray-400"
                : "border-gray-300 hover:bg-gray-50"
                }`}
            >
              Next
            </button>

          </div>
        </div>

        {/* {Model Box} */}
        {confirmModal.open && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div className="bg-white rounded-xl shadow-lg w-[90%] max-w-md p-6">
              <h2 className="text-xl font-bold text-gray-800">
                {confirmModal.title}
              </h2>

              <p className="mt-3 text-gray-600">{confirmModal.message}</p>

              <div className="flex justify-end gap-3 mt-6">
                <button
                  onClick={() =>
                    setConfirmModal((prev) => ({ ...prev, open: false }))
                  }
                  className="px-5 py-2 rounded-lg border border-gray-300 hover:bg-gray-100"
                >
                  Cancel
                </button>

                <button
                  onClick={confirmModal.action}
                  className="px-5 py-2 rounded-lg bg-[#0d4039] text-white hover:bg-[#0b322d]"
                >
                  Confirm
                </button>
              </div>
            </div>
          </div>
        )}

        {/* MODAL RESET */}
        {showModal && (
          <PermissionModalBox
            isOpen={showModal}
            onClose={() => {
              setShowModal(false);
              setSelectedPermission(null);
            }}
            mode={modalMode}
            permission={selectedPermission}
            onSuccess={loadPermissions}
          />
        )}
      </div>
    </div>
  );
}
