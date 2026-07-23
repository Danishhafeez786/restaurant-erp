import { useEffect, useState } from "react";
import axiosClient from "../../../api/axiosClient";
import BranchModalBox from "./BranchModalBox";
import FilterField from "../../../components/FilterField";
import CustomSelect from "../../../components/CustomSelect";
import { Listbox } from "@headlessui/react";
import {
  PlusIcon,
  EyeIcon,
  TrashIcon,
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

export default function BranchTable() {
  const [branches, setBranches] = useState([]);
  const [loading, setLoading] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");
  const [selectedBranch, setSelectedBranch] = useState(null);

  const user = JSON.parse(localStorage.getItem("user"));

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);

  const [sortBy, setSortBy] = useState("createdAt");
  const [direction, setDirection] = useState("DESC");

  const [searchCriteria, setSearchCriteria] = useState({
    searchInput: "",
    organizationId: "",
    isActive: "",
  });

  const [confirmModal, setConfirmModal] = useState({
    open: false,
    title: "",
    message: "",
    action: null,
  });

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
      title: "Delete Branch",
      message: "Are you sure you want to delete this branch?",
      action: async () => {
        try {
          await axiosClient.delete(`/branch/${id}`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to delete branch",
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
      title: "Restore Branch",
      message: "Are you sure you want to restore this branch?",
      action: async () => {
        try {
          await axiosClient.patch(`/branch/${id}/restore`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to restore branch",
          );
        } finally {
          setConfirmModal((prev) => ({ ...prev, open: false }));
        }
      },
    });
  };

  const loadBranches = async () => {
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
        `/branch/search?page=${currentPage}&size=${pageSize}&sortBy=${sortBy}&direction=${direction}`,
        payload,
      );

      const page = response.data.data;

      setBranches(page.content);
      setTotalPages(page.totalPages);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadBranches();
  }, [currentPage, pageSize, sortBy, direction]);

  useEffect(() => {
    const timer = setTimeout(() => {
      setCurrentPage(0);
      loadBranches();
    }, 300);

    return () => clearTimeout(timer);
  }, [searchCriteria, sortBy, direction, pageSize]);

  useEffect(() => {
    const eventSource = new EventSource(`${API_URL}/branch/stream`);

    eventSource.addEventListener("branch-created", () => {
      loadBranches();
      toast.success("A new branch was created.");
    });

    eventSource.addEventListener("branch-updated", () => {
      loadBranches();
      toast.success("A branch was updated.");
    });

    eventSource.addEventListener("branch-deleted", async (event) => {
      loadBranches();
      toast.success("A branch was deleted.");
    });

    eventSource.addEventListener("branch-restored", () => {
      loadBranches();
      toast.success("A branch was restored.");
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
          <h2 className="text-2xl font-bold">Branches</h2>

          {user.permissions.includes("BRANCH_CREATE") && (
            <button
              onClick={() => {
                setModalMode("create");
                setSelectedBranch(null);
                setShowModal(true);
              }}
              className="px-3 sm:px-6 py-2 bg-[#0d4039] text-white rounded-lg font-medium flex items-center justify-center gap-2"
            >
              <PlusIcon className="w-5 h-5" title="Add Branch" />
              <span className="hidden sm:inline">Add Branch</span>
            </button>
          )}
        </div>
        {/* Filters */}

        <div>
          <div className="flex flex-wrap items-center gap-4">
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
                placeholder="Role Name, Organization, Description..."
                className="w-full border-0 bg-transparent text-sm focus:outline-none"
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
              className="w-full xl:flex-1 xl:min-w-[50px]"
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
              className="flex h-12 w-full items-center justify-center gap-2 rounded-xl bg-gray-100 px-6 font-bold transition hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300 md:w-auto"
            >
              <ArrowPathIcon className="h-5 w-5" />
              <span>Reset</span>
            </button>
          </div>
        </div>
      </div>

      {/* Table */}
      <br />
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        <div className=" flex items-center justify-between mb-4 ">
          <p className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-green-600">
              {branches.length}
            </span>{" "}
            Branches
          </p>
        </div>
        <div className="hidden lg:block overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
          <table className="min-w-full">
            <thead className="border-b border-gray-200 bg-gray-50">
              <tr className="bg-gray-100">
                <SortableHeader label="Branch Name" field="branchName" />
                <SortableHeader label="Branch Code" field="branchCode" />
                <SortableHeader label="Address" field="address" />
                <SortableHeader label="City" field="city" />
                <SortableHeader label="Phone" field="phone" />
                <SortableHeader label="Organization" field="organization" />
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
                branches.map((branch) => (
                  <tr key={branch.id} className="border-b hover:bg-gray-50">
                    <td className="px-4 py-3 ">{branch.branchName}</td>

                    <td className="px-4 py-3 ">{branch.branchCode}</td>

                    <td className="px-4 py-3 ">{branch.address}</td>

                    <td className="px-4 py-3 ">{branch.city}</td>

                    <td className="px-4 py-3 ">{branch.phone}</td>

                    <td className="px-4 py-3 ">
                      {branch.organizationModel?.organizationName || "-"}
                    </td>

                    <td className="px-4 py-3 ">
                      <span
                        className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                          branch.isActive,
                        )}`}
                      >
                        {branch.isActive ? "ACTIVE" : "INACTIVE"}
                      </span>
                    </td>

                    <td className="px-4 py-3 ">
                      {user.permissions.includes("BRANCH_VIEW") && (
                        <button
                          className="text-Balck-600"
                          disabled={!user.permissions.includes("BRANCH_VIEW")}
                          onClick={() => {
                            setSelectedBranch(branch);
                            setModalMode("view");
                            setShowModal(true);
                          }}
                          className="text-green-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1"
                        >
                          <EyeIcon className="w-5 h-5" title="View" />
                        </button>
                      )}

                      {user.permissions.includes("BRANCH_UPDATE") && (
                        <button
                          className="text-blue-600"
                          onClick={() => {
                            setSelectedBranch(branch);
                            setModalMode("edit");
                            setShowModal(true);
                          }}
                          className="text-blue-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                        >
                          <PenLine className="w-5 h-5" title="Edit" />
                        </button>
                      )}

                      {branch.isActive
                        ? user?.permissions?.includes("BRANCH_DELETE") && (
                          <button
                            className="text-Balck-600"
                            onClick={() => openDeleteModal(branch.id)}
                            className="text-red-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                          >
                            <Trash className="w-5 h-5 " title="Delete" />
                          </button>
                        )
                        : user?.permissions?.includes("BRANCH_REACTIVATE") && (
                          <button
                            className="text-orange-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                            onClick={() => openRestoreModal(branch.id)}
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

        {/* MOBILE */}
        <div className="md:hidden space-y-4">
          {loading ? (
            <div className="text-center py-10 bg-white rounded-xl border">
              Loading...
            </div>
          ) : (
            branches.map((branch) => (
              <div key={branch.id} className="border rounded-xl p-4 bg-white">
                <div className="flex justify-between items-start">
                  <h3 className="font-bold text-lg">{branch.branchName}</h3>

                  <span
                    className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                      branch.isActive,
                    )}`}
                  >
                    {branch.isActive ? "ACTIVE" : "INACTIVE"}
                  </span>
                </div>

                <div className="mt-3 space-y-2 text-sm">
                  <p>
                    <b>Branch Code:</b> {branch.branchCode}
                  </p>

                  <p>
                    <b>Address:</b> {branch.address}
                  </p>

                  <p>
                    <b>City:</b> {branch.city}
                  </p>

                  <p>
                    <b>Phone:</b> {branch.phone}
                  </p>

                  <p>
                    <b>Organization:</b>{" "}
                    {branch.organizationModel?.organizationName || "N/A"}
                  </p>
                </div>

                <div className="flex gap-2 mt-4">
                  {user.permissions.includes("BRANCH_VIEW") && (
                    <button
                      className="flex-1 bg-green-500 text-white py-2 rounded-lg"
                      onClick={() => {
                        setSelectedBranch(branch);
                        setModalMode("view");
                        setShowModal(true);
                      }}
                    >
                      View
                    </button>
                  )}

                  {user.permissions.includes("BRANCH_UPDATE") && (
                    <button
                      className="flex-1 bg-blue-500 text-white py-2 rounded-lg"
                      onClick={() => {
                        setSelectedBranch(branch);
                        setModalMode("edit");
                        setShowModal(true);
                      }}
                    >
                      Edit
                    </button>
                  )}
                  {branch.isActive
                    ? user.permissions.includes("BRANCH_DELETE") && (
                      <button
                        className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                        onClick={() => openDeleteModal(branch.id)}
                      >
                        Delete
                      </button>
                    )
                    : user.permissions.includes("BRANCH_REACTIVATE") && (
                      <button
                        className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                        disabled={
                          !user?.permissions?.includes("BRANCH_REACTIVATE")
                        }
                        onClick={() => openRestoreModal(branch.id)}
                      >
                        Restore
                      </button>
                    )}
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
            <span className="font-semibold text-green-600">
              {branches.length === 0 ? 0 : currentPage * pageSize + 1}
            </span>{" "}
            to{" "}
            <span className="font-semibold text-green-600">
              {Math.min(
                (currentPage + 1) * pageSize,
                currentPage * pageSize + branches.length,
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
      ${currentPage === 0
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

        ${currentPage === index
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

      ${currentPage === totalPages - 1 || totalPages === 0
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

        {/* Modal */}

        <BranchModalBox
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          mode={modalMode}
          branch={selectedBranch}
          onSuccess={loadBranches}
        />
      </div>
    </div>
  );
}
