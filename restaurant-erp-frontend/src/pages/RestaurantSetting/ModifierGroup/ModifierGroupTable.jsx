import { useEffect, useState } from "react";
import axiosClient from "../../../api/axiosClient";
import ModifierGroupModelBox from "./ModifierGroupModelBox";
import FilterField from "../../../components/FilterField";
import CustomSelect from "../../../components/CustomSelect";
import modifierGroupService from "../../../services/modifierGroupService";

import {
  PlusIcon,
  EyeIcon,
  MagnifyingGlassIcon,
  FunnelIcon,
  ArrowPathIcon,
  ChevronUpIcon,
  ChevronDownIcon,
} from "@heroicons/react/24/outline";

import { Trash, PenLine } from "lucide-react";

import { toast } from "react-toastify";

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

export default function ModifierGroupTable() {
  const [showFilters, setShowFilters] = useState(false);

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
    searchInput: "",
    organizationId: "",
    isActive: "",
  });

  const user = JSON.parse(localStorage.getItem("user"));

  const canCreate = user?.permissions?.includes("MODIFIER_GROUP_CREATE");

  const canView = user?.permissions?.includes("MODIFIER_GROUP_VIEW");

  const canUpdate = user?.permissions?.includes("MODIFIER_GROUP_UPDATE");

  const canDelete = user?.permissions?.includes("MODIFIER_GROUP_DELETE");

  const canRestore = user?.permissions?.includes("MODIFIER_GROUP_REACTIVATE");

  const handleSort = (field) => {
    setCurrentPage(0);

    if (sortBy === field) {
      setDirection((prev) => (prev === "ASC" ? "DESC" : "ASC"));
    } else {
      setSortBy(field);
      setDirection("ASC");
    }
  };

  const [confirmModal, setConfirmModal] = useState({
    open: false,
    title: "",
    message: "",
    action: null,
  });

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
      title: "Delete Modifier Group",
      message: "Are you sure you want to delete this modifier group?",
      action: async () => {
        try {
          await modifierGroupService.delete(id);
          toast.success("Modifier Group Deleted Successfully");
          loadModifierGroups();
        } catch (error) {
          toast.error(
            error?.response?.data?.message ||
              "Failed to delete modifier group.",
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
      title: "Restore Modifier Group",
      message: "Are you sure you want to restore this modifier group?",
      action: async () => {
        try {
          await modifierGroupService.restore(id);
          toast.success("Modifier Group Restored Successfully");
          loadModifierGroups();
        } catch (error) {
          toast.error(
            error?.response?.data?.message ||
              "Failed to restore modifier group.",
          );
        } finally {
          setConfirmModal((prev) => ({ ...prev, open: false }));
        }
      },
    });
  };

  const loadModifierGroups = async () => {
    try {
      setLoading(true);

      const payload = {
        searchInput: searchCriteria.searchInput || null,

        organizationId: searchCriteria.organizationId || null,

        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
      };

      const response = await modifierGroupService.search(
        payload,
        currentPage,
        pageSize,
        sortBy,
        direction,
      );

      setModifierGroups(response.content);

      setTotalPages(response.totalPages);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadModifierGroups();
  }, [currentPage, pageSize, sortBy, direction]);

  useEffect(() => {
    const timer = setTimeout(() => {
      setCurrentPage(0);
      loadModifierGroups();
    }, 300);

    return () => clearTimeout(timer);
  }, [searchCriteria, sortBy, direction, pageSize]);

  useEffect(() => {
    const eventSource = new EventSource(`${API_URL}/modifier-group/stream`);

    eventSource.addEventListener("modifier-group-created", () => {
      loadModifierGroups();
      toast.success("A modifier group was created.");
    });

    eventSource.addEventListener("modifier-group-updated", () => {
      loadModifierGroups();
      toast.success("A modifier group was updated.");
    });

    eventSource.addEventListener("modifier-group-deleted", () => {
      loadModifierGroups();
      toast.success("A modifier group was deleted.");
    });

    eventSource.addEventListener("modifier-group-restored", () => {
      loadModifierGroups();
      toast.success("A modifier group was restored.");
    });

    return () => eventSource.close();
  }, []);

  const resetFilters = () => {
    setSearchCriteria({
      searchInput: "",
      organizationId: "",
      isActive: "",
    });

    setSortBy("createdAt");
    setDirection("DESC");
    setCurrentPage(0);
  };

  const getStatusColor = (status) =>
    status ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700";

  return (
    <div>
      {/* =========================== HEADER =========================== */}

      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold">Modifier Groups</h2>

          {canCreate && (
            <button
              onClick={() => {
                setModalMode("create");
                setSelectedModifierGroup(null);
                setShowModal(true);
              }}
              className="px-3 sm:px-6 py-2 bg-[#0d4039] text-white rounded-lg font-medium flex items-center justify-center gap-2"
            >
              <PlusIcon className="w-5 h-5" />
              <span className="hidden sm:inline">Add Modifier Group</span>
            </button>
          )}
        </div>

        {/* Search & Filters */}

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
                placeholder="Modifier Group Name, Code"
                className="w-full rounded-xl border border-gray-300 bg-white py-3 pl-11 pr-12 text-sm shadow-sm outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
              />

              <MagnifyingGlassIcon className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />

              <button
                onClick={() => setShowFilters(!showFilters)}
                className="absolute right-2 top-1/2 -translate-y-1/2 rounded-lg p-2 hover:bg-gray-100"
              >
                <FunnelIcon
                  className={`h-5 w-5 transition ${
                    showFilters ? "rotate-180 text-blue-600" : "text-gray-500"
                  }`}
                />
              </button>
            </div>
          </div>

          {/* Mobile Filters */}

          <div
            className={`lg:hidden overflow-hidden transition-all duration-300 ${
              showFilters
                ? "max-h-[500px] opacity-100 mt-4"
                : "max-h-0 opacity-0"
            }`}
          >
            <div className="space-y-4">
              {showFilters && (
                <>
                  <FilterField label="Status">
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

                  <FilterField label="Order">
                    <CustomSelect
                      options={orderOptions}
                      value={direction}
                      onChange={(item) => {
                        setCurrentPage(0);
                        setDirection(item.value);
                      }}
                    />
                  </FilterField>

                  <FilterField label="Rows">
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
                    onClick={resetFilters}
                    className="w-full h-12 rounded-xl bg-gray-100 hover:bg-gray-200 font-medium flex items-center justify-center gap-2"
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
                placeholder="Modifier Group Name, Code"
                className="w-full border-0 bg-transparent text-sm focus:outline-none"
              />
            </FilterField>

            <FilterField
              label="Status"
              className="w-full xl:flex-1 xl:min-w-[220px]"
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

            <FilterField
              label="Order"
              className="w-full xl:flex-1 xl:min-w-[220px]"
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

            <FilterField
              label="Rows"
              className="w-full xl:flex-1 xl:min-w-[120px]"
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
              onClick={resetFilters}
              className="h-12 px-6 rounded-xl bg-gray-100 hover:bg-gray-200 font-bold flex items-center gap-2"
            >
              <ArrowPathIcon className="h-5 w-5" />
              Reset
            </button>
          </div>
        </div>
      </div>

      {/* =========================== DESKTOP TABLE =========================== */}

      <br />
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        {/* =========================== Table Header =========================== */}
        <div className=" flex items-center justify-between mb-4 ">
          <p className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-blue-600">
              {modifierGroups.length}
            </span>{" "}
            Modifier Groups
          </p>
        </div>
        {/* =========================== DESKTOP TABLE =========================== */}

        <div className="hidden lg:block overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
          <table className="min-w-full">
            <thead className="border-b border-gray-200 bg-gray-50">
              <tr className="bg-gray-100">
                <SortableHeader label="Code" field="code" />
                <SortableHeader label="Name" field="name" />
                <SortableHeader label="Description" field="description" />
                <SortableHeader label="Min Selection" field="minSelection" />
                <SortableHeader label="Max Selection" field="maxSelection" />
                <SortableHeader label="Required" field="required" />
                <SortableHeader label="Organization" field="organization" />
                <SortableHeader label="Branch" field="branch" />
                <SortableHeader label="Status" field="isActive" />
                <th className="rounded-tr-xl px-4 py-3 text-left">Action</th>
              </tr>
            </thead>

            <tbody>
              {loading && (
                <tr>
                  <td colSpan="10" className="py-10 text-center">
                    Loading...
                  </td>
                </tr>
              )}

              {!loading && modifierGroups.length === 0 && (
                <tr>
                  <td colSpan="10" className="py-10 text-center text-gray-500">
                    No Modifier Groups Found
                  </td>
                </tr>
              )}

              {!loading &&
                modifierGroups.map((modifierGroup) => (
                  <tr
                    key={modifierGroup.id}
                    className="border-b hover:bg-gray-50"
                  >
                    <td className="px-4 py-3">{modifierGroup.code}</td>

                    <td className="px-4 py-3">
                      <div className="font-medium">{modifierGroup.name}</div>
                    </td>

                    <td className="px-4 py-3">
                      {modifierGroup.description || "-"}
                    </td>

                    <td className="px-4 py-3">{modifierGroup.minSelection}</td>

                    <td className="px-4 py-3">{modifierGroup.maxSelection}</td>

                    <td className="px-4 py-3">
                      <span
                        className={`px-3 py-1 rounded-full text-sm ${
                          modifierGroup.required
                            ? "bg-blue-100 text-blue-700"
                            : "bg-gray-200 text-gray-700"
                        }`}
                      >
                        {modifierGroup.required ? "Required" : "Optional"}
                      </span>
                    </td>

                    <td className="px-4 py-3">
                      {modifierGroup.organizationModel?.organizationName || "-"}
                    </td>

                    <td className="px-4 py-3">
                      {modifierGroup.branchModel?.branchName || "-"}
                    </td>

                    <td className="px-4 py-3">
                      <span
                        className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                          modifierGroup.isActive,
                        )}`}
                      >
                        {modifierGroup.isActive ? "ACTIVE" : "INACTIVE"}
                      </span>
                    </td>

                    <td className="px-4 py-3">
                      {canView && (
                        <button
                          onClick={() => {
                            setSelectedModifierGroup(modifierGroup);
                            setModalMode("view");
                            setShowModal(true);
                          }}
                          className="text-green-600 mr-3 hover:bg-gray-50 border rounded-lg p-1"
                        >
                          <EyeIcon className="w-5 h-5" title="View" />
                        </button>
                      )}

                      {canUpdate && (
                        <button
                          onClick={() => {
                            setSelectedModifierGroup(modifierGroup);
                            setModalMode("edit");
                            setShowModal(true);
                          }}
                          className="text-blue-600 mr-3 hover:bg-gray-50 border rounded-lg p-1"
                        >
                          <PenLine className="w-5 h-5" title="Edit" />
                        </button>
                      )}

                      {modifierGroup.isActive
                        ? canDelete && (
                            <button
                              onClick={() => openDeleteModal(modifierGroup.id)}
                              className="text-red-600 mr-3 hover:bg-gray-50 border rounded-lg p-1"
                            >
                              <Trash className="w-5 h-5" title="Delete" />
                            </button>
                          )
                        : canRestore && (
                            <button
                              onClick={() => openRestoreModal(modifierGroup.id)}
                              className="text-orange-600 mr-3 hover:bg-gray-50 border rounded-lg p-1"
                            >
                              <ArrowPathIcon
                                className="w-5 h-5"
                                title="Restore"
                              />
                            </button>
                          )}
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>

        {/* =========================== MOBILE VIEW =========================== */}

        <div className="lg:hidden rounded-2xl border border-gray-200 bg-white overflow-hidden shadow-sm">
          {loading ? (
            <div className="text-center py-10">Loading...</div>
          ) : modifierGroups.length === 0 ? (
            <div className="text-center py-10 text-gray-500">
              No Modifier Groups Found
            </div>
          ) : (
            modifierGroups.map((modifierGroup) => (
              <div
                key={modifierGroup.id}
                className="px-4 py-3 border-b last:border-b-0 hover:bg-blue-50/40 transition"
              >
                {/* Header */}

                <div className="flex items-start justify-between gap-3">
                  <div className="flex min-w-0 flex-1 gap-3">
                    <div className="flex h-14 w-14 items-center justify-center rounded-lg border bg-[#0d4039]/10 text-[#0d4039] font-bold text-xl">
                      {modifierGroup.name?.charAt(0)?.toUpperCase()}
                    </div>

                    <div className="min-w-0 flex-1">
                      <div className="flex items-center gap-2">
                        <h3 className="truncate font-semibold text-gray-900">
                          {modifierGroup.name}
                        </h3>

                        <span
                          className={`rounded-full px-2 py-0.5 text-[10px] font-semibold ${
                            modifierGroup.isActive
                              ? "bg-green-100 text-green-700"
                              : "bg-red-100 text-red-700"
                          }`}
                        >
                          {modifierGroup.isActive ? "ACTIVE" : "INACTIVE"}
                        </span>
                      </div>

                      <p className="mt-1 text-xs text-gray-500">
                        {modifierGroup.code}
                      </p>
                    </div>
                  </div>
                </div>

                {/* Details */}

                <div className="mt-3 space-y-1 text-sm">
                  <p>
                    <b>Description:</b> {modifierGroup.description || "-"}
                  </p>

                  <p>
                    <b>Min Selection:</b> {modifierGroup.minSelection}
                  </p>

                  <p>
                    <b>Max Selection:</b> {modifierGroup.maxSelection}
                  </p>

                  <p>
                    <b>Required:</b> {modifierGroup.required ? "Yes" : "No"}
                  </p>

                  <p>
                    <b>Organization:</b>{" "}
                    {modifierGroup.organizationModel?.organizationName || "N/A"}
                  </p>

                  <p>
                    <b>Branch:</b>{" "}
                    {modifierGroup.branchModel?.branchName || "N/A"}
                  </p>
                </div>

                {/* Actions */}

                <div className="mt-3 flex items-center justify-end gap-1">
                  {canView && (
                    <button
                      onClick={() => {
                        setSelectedModifierGroup(modifierGroup);
                        setModalMode("view");
                        setShowModal(true);
                      }}
                      className="rounded-lg p-2 text-green-600 hover:bg-green-100"
                    >
                      <EyeIcon className="h-5 w-5" />
                    </button>
                  )}

                  {canUpdate && (
                    <button
                      onClick={() => {
                        setSelectedModifierGroup(modifierGroup);
                        setModalMode("edit");
                        setShowModal(true);
                      }}
                      className="rounded-lg p-2 text-indigo-600 hover:bg-indigo-100"
                    >
                      <PenLine className="h-5 w-5" />
                    </button>
                  )}

                  {modifierGroup.isActive
                    ? canDelete && (
                        <button
                          onClick={() => openDeleteModal(modifierGroup.id)}
                          className="rounded-lg p-2 text-red-600 hover:bg-red-100"
                        >
                          <Trash className="h-5 w-5" />
                        </button>
                      )
                    : canRestore && (
                        <button
                          onClick={() => openRestoreModal(modifierGroup.id)}
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

        {/* =========================== PAGINATION =========================== */}

        <div className="mt-6 flex flex-col gap-4 border-t border-gray-200 pt-5 md:flex-row md:items-center md:justify-between">
          {/* Left */}

          <div className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-blue-600">
              {modifierGroups.length === 0 ? 0 : currentPage * pageSize + 1}
            </span>{" "}
            to{" "}
            <span className="font-semibold text-blue-600">
              {Math.min(
                (currentPage + 1) * pageSize,
                currentPage * pageSize + modifierGroups.length,
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
              className={`rounded-xl border px-4 py-2 text-sm transition
      ${
        currentPage === 0
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
                className={`h-10 w-10 rounded-xl text-sm font-medium transition

        ${
          currentPage === index
            ? "bg-[#0d4039] text-white shadow-sm"
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
              className={`rounded-xl border px-4 py-2 text-sm transition

      ${
        currentPage === totalPages - 1 || totalPages === 0
          ? "cursor-not-allowed border-gray-200 text-gray-400"
          : "border-gray-300 hover:bg-gray-50"
      }`}
            >
              Next
            </button>
          </div>
        </div>

        {/* =========================== MODIFIER GROUP MODAL =========================== */}

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

        <ModifierGroupModelBox
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          mode={modalMode}
          modifierGroup={selectedModifierGroup}
          onSuccess={loadModifierGroups}
        />
      </div>
    </div>
  );
}
