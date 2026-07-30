import { useEffect, useState } from "react";
import axiosClient from "../../../api/axiosClient";
import CategoryModelBox from "./CategoryModelBox";
import FilterField from "../../../components/FilterField";
import CustomSelect from "../../../components/CustomSelect";
import CategoryService from "../../../services/CategoryService";
import { Listbox } from "@headlessui/react";
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
import { toast } from "react-toastify";
import { PenLine } from "lucide-react";

export default function CategoryTable() {
  const [showFilters, setShowFilters] = useState(false);
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
    searchInput: "",
    organizationId: "",
    isActive: "",
  });

  const user = JSON.parse(localStorage.getItem("user"));

  const canCreate = user?.permissions?.includes("CATEGORY_CREATE");

  const canView = user?.permissions?.includes("CATEGORY_VIEW");

  const canUpdate = user?.permissions?.includes("CATEGORY_UPDATE");

  const canDelete = user?.permissions?.includes("CATEGORY_DELETE");

  const canRestore = user?.permissions?.includes("CATEGORY_REACTIVATE");

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
      title: "Delete Category",
      message: "Are you sure you want to delete this category?",
      action: async () => {
        try {
          await axiosClient.delete(`/category/${id}`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to delete category",
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
      title: "Restore Category",
      message: "Are you sure you want to restore this category?",
      action: async () => {
        try {
          await axiosClient.patch(`/category/${id}/restore`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to restore category",
          );
        } finally {
          setConfirmModal((prev) => ({ ...prev, open: false }));
        }
      },
    });
  };

  const loadCategories = async () => {
    try {
      setLoading(true);
      const payload = {
        searchInput: searchCriteria.searchInput || null,
        organizationId: searchCriteria.organizationId || null,
        available:
          searchCriteria.available === ""
            ? null
            : searchCriteria.available === "true",

        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
      };

      const response = await CategoryService.search(
        payload,
        currentPage,
        pageSize,
        sortBy,
        direction,
      );
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
  }, [currentPage, pageSize, sortBy, direction]);

  useEffect(() => {
    const timer = setTimeout(() => {
      setCurrentPage(0);
      loadCategories();
    }, 300);

    return () => clearTimeout(timer);
  }, [searchCriteria, sortBy, direction, pageSize]);

  useEffect(() => {
    const eventSource = new EventSource(`${API_URL}/category/stream`);

    eventSource.addEventListener("category-created", () => {
      loadCategories();
      toast.success("A new category was created.");
    });

    eventSource.addEventListener("category-updated", () => {
      loadCategories();
      toast.success("A category was updated.");
    });

    eventSource.addEventListener("category-deleted", async (event) => {
      loadCategories();
      toast.success("A category was deleted.");
    });

    eventSource.addEventListener("category-restored", () => {
      loadCategories();
      toast.success("A category was restored.");
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
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        {/* Header */}

        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold">Categories</h2>

          {canCreate && (
            <button
              onClick={() => {
                setModalMode("create");
                setSelectedCategory(null);
                setShowModal(true);
              }}
              className="px-3 sm:px-6 py-2 bg-[#0d4039] text-white rounded-lg font-medium flex items-center justify-center gap-2"
            >
              <PlusIcon className="w-5 h-5" />
              <span className="hidden sm:inline">Add Category</span>
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
                placeholder="Category Name, Category Code"
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
            className={`lg:hidden overflow-hidden transition-all duration-300 ease-in-out ${
              showFilters
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
                placeholder="Category Name, Category Code"
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
      
      <br />
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        {/* =========================== Table Header =========================== */}
        <div className=" flex items-center justify-between mb-4 ">
          <p className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-blue-600">
              {categories.length}
            </span>{" "}
            Categories
          </p>
        </div>
        {/* =========================== DESKTOP TABLE =========================== */}
        <div className="hidden lg:block overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
          <table className="min-w-full">
            <thead className="border-b border-gray-200 bg-gray-50">
              <tr className="bg-gray-100">
                <SortableHeader label="Category Code" field="categoryCode" />
                <SortableHeader label="Category Name" field="categoryName" />
                <SortableHeader label="Description" field="description" />
                <SortableHeader label="Display Order" field="displayOrder" />
                <SortableHeader label="Available" field="available" />
                <SortableHeader label="Organization" field="organization" />
                <SortableHeader label="Branch" field="branch" />
                <SortableHeader label="Status" field="isActive" />
                <th className="rounded-tr-xl px-4 py-3 text-left">Action</th>
              </tr>
            </thead>

            <tbody>
              {loading && (
                <tr>
                  <td colSpan="8" className="text-center py-10">
                    Loading...
                  </td>
                </tr>
              )}

              {!loading &&
                categories.map((category) => (
                  <tr key={category.id} className="border-b hover:bg-gray-50">
                    <td className="px-4 py-3 ">{category.categoryCode}</td>

                    <td className="px-4 py-3 ">
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
                        </div>
                      </div>
                    </td>

                    <td className="px-4 py-3 ">{category.description}</td>

                    <td className="px-4 py-3 ">{category.displayOrder}</td>

                    <td className="px-4 py-3 ">
                      <span
                        className={`px-3 py-1 rounded-full text-sm ${
                          category.available
                            ? "bg-blue-100 text-blue-700"
                            : "bg-gray-200 text-gray-700"
                        }`}
                      >
                        {category.available ? "Available" : "Unavailable"}
                      </span>
                    </td>

                    <td className="px-4 py-3 ">
                      {category.organizationModel?.organizationName || "-"}
                    </td>

                    <td className="px-4 py-3 ">
                      {category.branchModel?.branchName || "-"}
                    </td>

                    <td className="px-4 py-3 ">
                      <span
                        className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                          category.isActive,
                        )}`}
                      >
                        {category.isActive ? "ACTIVE" : "INACTIVE"}
                      </span>
                    </td>

                    <td className="px-4 py-3 ">
                      {canView && (
                        <button
                          className="text-Balck-600"
                          onClick={() => {
                            setSelectedCategory(category);
                            setModalMode("view");
                            setShowModal(true);
                          }}
                          className="text-green-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1"
                        >
                          <EyeIcon className="w-5 h-5" title="View" />
                        </button>
                      )}

                      {canUpdate && (
                        <button
                          className="text-blue-600"
                          onClick={() => {
                            setSelectedCategory(category);
                            setModalMode("edit");
                            setShowModal(true);
                          }}
                          className="text-blue-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                        >
                          <PenLine className="w-5 h-5" title="Edit" />
                        </button>
                      )}

                      {category.isActive
                        ? canDelete && (
                            <button
                              className="text-Balck-600"
                              onClick={() => openDeleteModal(category.id)}
                              className="text-red-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                            >
                              <Trash className="w-5 h-5 " title="Delete" />
                            </button>
                          )
                        : canRestore && (
                            <button
                              className="text-orange-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                              onClick={() => openRestoreModal(category.id)}
                            >
                              <ArrowPathIcon
                                className="w-5 h-5 "
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
          ) : categories.length === 0 ? (
            <div className="text-center py-10 text-gray-500">
              No Categories Found
            </div>
          ) : (
            categories.map((category) => (
              <div
                key={category.id}
                className="px-4 py-3 border-b last:border-b-0 hover:bg-blue-50/40 transition"
              >
                {/* Header */}
                <div className="flex items-start justify-between gap-3">
                  <div className="flex min-w-0 flex-1 gap-3">
                    {category.imageUrl ? (
                      <img
                        src={category.imageUrl}
                        alt={category.categoryName}
                        className="h-14 w-14 rounded-lg border object-cover"
                      />
                    ) : (
                      <div className="flex h-14 w-14 items-center justify-center rounded-lg border bg-gray-100 text-[10px] text-gray-400">
                        No Image
                      </div>
                    )}

                    <div className="min-w-0 flex-1">
                      <div className="flex items-center gap-2">
                        <h3 className="truncate font-semibold text-gray-900">
                          {category.categoryName}
                        </h3>

                        <span
                          className={`rounded-full px-2 py-0.5 text-[10px] font-semibold ${
                            category.isActive
                              ? "bg-green-100 text-green-700"
                              : "bg-red-100 text-red-700"
                          }`}
                        >
                          {category.isActive ? "ACTIVE" : "INACTIVE"}
                        </span>
                      </div>

                      <p className="mt-1 text-xs text-gray-500">
                        {category.categoryCode}
                      </p>
                    </div>
                  </div>
                </div>

                {/* Details */}
                <div className="mt-3 space-y-1 text-sm">
                  <p>
                    <b>Description:</b> {category.description || "-"}
                  </p>

                  <p>
                    <b>Display Order:</b> {category.displayOrder}
                  </p>

                  <p>
                    <b>Available:</b> {category.available ? "Yes" : "No"}
                  </p>

                  <p>
                    <b>Organization:</b>{" "}
                    {category.organizationModel?.organizationName || "N/A"}
                  </p>

                  <p>
                    <b>Branch:</b> {category.branchModel?.branchName || "N/A"}
                  </p>
                </div>

                {/* Actions */}
                <div className="mt-3 flex items-center justify-end gap-1">
                  {canView && (
                    <button
                      onClick={() => {
                        setSelectedCategory(category);
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
                        setSelectedCategory(category);
                        setModalMode("edit");
                        setShowModal(true);
                      }}
                      className="rounded-lg p-2 text-indigo-600 hover:bg-indigo-100"
                    >
                      <PenLine className="h-5 w-5" />
                    </button>
                  )}

                  {category.isActive
                    ? canDelete && (
                        <button
                          onClick={() => openDeleteModal(category.id)}
                          className="rounded-lg p-2 text-red-600 hover:bg-red-100"
                        >
                          <Trash className="h-5 w-5" />
                        </button>
                      )
                    : canRestore && (
                        <button
                          onClick={() => openRestoreModal(category.id)}
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
              {categories.length === 0 ? 0 : currentPage * pageSize + 1}
            </span>{" "}
            to{" "}
            <span className="font-semibold text-blue-600">
              {Math.min(
                (currentPage + 1) * pageSize,
                currentPage * pageSize + categories.length,
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

        {/* =========================== MODAL =========================== */}

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

        <CategoryModelBox
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          mode={modalMode}
          category={selectedCategory}
          onSuccess={loadCategories}
        />
      </div>
    </div>
  );
}
