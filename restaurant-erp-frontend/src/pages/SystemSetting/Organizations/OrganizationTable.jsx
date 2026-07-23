import { useEffect, useState } from "react";
import axiosClient from "../../../api/axiosClient";
import FilterField from "../../../components/FilterField";
import CustomSelect from "../../../components/CustomSelect";
import {
  PlusIcon,
  EyeIcon,
  PencilSquareIcon,
  TrashIcon,
  ArrowPathIcon,
  ChevronUpIcon,
  ChevronDownIcon,
  ChevronUpDownIcon,
  CheckIcon,
} from "@heroicons/react/24/outline";

import {
  Trash,
  ScanEye,
  PenLine,
} from "lucide-react";

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
import OrganizationModalBox from "./OrganizationModalBox";
const API_URL = import.meta.env.VITE_API_URL;
import { toast } from "react-toastify";

export default function OrganizationTable() {
  const [organizations, setOrganizations] = useState([]);
  const [loading, setLoading] = useState(false);

  const user = JSON.parse(localStorage.getItem("user"));

  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");

  const [selectedOrganization, setSelectedOrganization] = useState(null);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [pageSize, setPageSize] = useState(10);

  const [sortBy, setSortBy] = useState("createdAt");

  const [direction, setDirection] = useState("DESC");

  const [searchCriteria, setSearchCriteria] = useState({
    searchInput: "",
    isActive: "",
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

  const [confirmModal, setConfirmModal] = useState({
    open: false,
    title: "",
    message: "",
    action: null,
  });

  const openDeleteModal = (id) => {
    setConfirmModal({
      open: true,
      title: "Delete Organization",
      message: "Are you sure you want to delete this organization?",
      action: async () => {
        try {
          await axiosClient.delete(`/organization/${id}`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to delete organization",
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
      title: "Restore Organization",
      message: "Are you sure you want to restore this organization?",
      action: async () => {
        try {
          await axiosClient.patch(`/organization/${id}/restore`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to restore organization",
          );
        } finally {
          setConfirmModal((prev) => ({ ...prev, open: false }));
        }
      },
    });
  };

  // ===== LOAD DATA =====
  const loadOrganizations = async () => {
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
        `/organization/search?page=${currentPage}&size=${pageSize}&sortBy=${sortBy}&direction=${direction}`,
        payload,
      );

      const pageData = response.data.data;

      setOrganizations(pageData.content);
      setTotalPages(pageData.totalPages);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  // ===== INIT LOAD =====
  useEffect(() => {
    loadOrganizations();
  }, [currentPage, pageSize, sortBy, direction]);

  useEffect(() => {
    const timer = setTimeout(() => {
      setCurrentPage(0);
      loadOrganizations();
    }, 300);

    return () => clearTimeout(timer);
  }, [searchCriteria, sortBy, direction, pageSize]);

  useEffect(() => {
    const eventSource = new EventSource(`${API_URL}/organization/stream`);

    eventSource.addEventListener("organization-created", () => {
      loadOrganizations();
      toast.success("A new organization was created.");
    });

    eventSource.addEventListener("organization-updated", () => {
      loadOrganizations();
      toast.success("An organization was updated.");
    });

    eventSource.addEventListener("organization-deleted", async (event) => {
      loadOrganizations();
      toast.success("An organization was deleted.");
    });

    eventSource.addEventListener("organization-restored", () => {
      loadOrganizations();
      toast.success("An organization was restored.");
    });

    return () => eventSource.close();
  }, []);

  const getStatusColor = (status) =>
    status ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700";

  return (
    <div>
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        {/* HEADER */}
        <div className="flex justify-between items-center mb-5">
          <h2 className="text-2xl font-bold">Organizations</h2>

          {user?.permissions?.includes("ORGANIZATION_CREATE") && (
            <button
              onClick={() => {
                setModalMode("create");
                setSelectedOrganization(null);
                setShowModal(true);
              }}
              className="px-3 sm:px-6 py-2 bg-[#0d4039] text-white rounded-lg font-medium flex items-center justify-center gap-2"
            >
              <PlusIcon className="w-5 h-5" title="Add Organization" />
              <span className="hidden sm:inline">Add Organization</span>
            </button>
          )}
        </div>

        {/* Search Toolbar */}
        <div className="flex flex-wrap items-center gap-4">
          <FilterField label="Search" className="flex-1 min-w-[500px]">
            <input
              type="text"
              value={searchCriteria.searchInput}
              onChange={(e) =>
                setSearchCriteria((prev) => ({
                  ...prev,
                  searchInput: e.target.value,
                }))
              }
              placeholder="Organization, Owner, City..."
              className="w-full border-0 bg-transparent text-sm focus:outline-none"
            />
          </FilterField>

          {/* Status */}
          <FilterField
            label="Status"
            className="w-full flex-1 min-w-[200px]"
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

          {/* Sort By */}
         

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
          <FilterField label="Rows" className="w-full xl:flex-1 xl:min-w-[100px]">
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
             className="w-full md:w-auto h-12 flex items-center justify-center gap-2 rounded-xl bg-gray-100 px-6 font-bold transition hover:bg-gray-200 on-slect-hover:outline-black focus:outline-none focus:ring-2 focus:ring-gray-300"
          >
            <ArrowPathIcon className="h-5 w-5" />
            Reset
          </button>
        </div>
      </div>

      <br />
      {/* TABLE */}
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        <div className=" flex items-center justify-between mb-4 ">
          <p className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-green-600">
              {organizations.length}
            </span>{" "}
            Organizations
          </p>
        </div>
        <div className="hidden lg:block overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
          <table className="min-w-full">
            <thead className="border-b border-gray-200 bg-gray-50">
              <tr className="bg-gray-100">
                <SortableHeader label="Organization" field="organization" />
                <SortableHeader label="Owner" field="ownerName" />
                <SortableHeader label="City" field="city" />
                <SortableHeader label="Country" field="country" />
                <SortableHeader label="Plan" field="subscriptionModel.name" />
                <SortableHeader label="Status" field="isActive" />
                <th className="rounded-tr-xl px-4 py-3 text-left">Action</th>
              </tr>
            </thead>

            <tbody>
              {organizations.map((org) => (
                <tr key={org.id} className="border-b hover:bg-gray-50">
                  <td className="px-4 py-3 ">
                    <div className="flex items-center gap-3">
                      {org.logoUrl ? (
                        <img
                          src={org.logoUrl}
                          alt={org.organizationName}
                          className="w-12 h-12 rounded-lg border object-cover flex-shrink-0"
                        />
                      ) : (
                        <div className="w-12 h-12 rounded-lg border flex items-center justify-center bg-gray-100 text-gray-400 text-xs flex-shrink-0">
                          No Image
                        </div>
                      )}

                      <span className="font-medium text-gray-800">
                        {org.organizationName}
                      </span>
                    </div>
                  </td>

                  <td className="px-4 py-3 ">{org.ownerName}</td>

                  <td className="px-4 py-3 ">{org.city}</td>

                  <td className="px-4 py-3 ">{org.country}</td>

                  <td className="px-4 py-3 ">
                    {org.subscriptionModel?.name || "N/A"}
                  </td>

                  <td className="px-4 py-3 ">
                    <span
                      className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                        org.isActive,
                      )}`}
                    >
                      {org.isActive ? "ACTIVE" : "INACTIVE"}
                    </span>
                  </td>

                  <td className="px-4 py-3 ">
                    {user?.permissions?.includes("ORGANIZATION_VIEW") && (
                      <button
                        onClick={() => {
                          setModalMode("view");
                          setSelectedOrganization(org);
                          setShowModal(true);
                        }}
                        className="text-green-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1"
                      >
                        <EyeIcon className="w-5 h-5" title="View" />
                      </button>
                    )}
                    {user?.permissions?.includes("ORGANIZATION_UPDATE") && (
                      <button
                        onClick={() => {
                          setModalMode("edit");
                          setSelectedOrganization(org);
                          setShowModal(true);
                        }}
                        className="text-blue-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                      >
                        <PenLine className="w-5 h-5" title="Edit" />
                      </button>
                    )}

                    {org.isActive &&
                      user?.permissions?.includes("ORGANIZATION_DELETE") && (
                        <button
                          onClick={() => openDeleteModal(org.id)}
                          className="text-red-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                        >
                          <Trash className="w-5 h-5" title="Delete" />
                        </button>
                      )}

                    {!org.isActive &&
                      user?.permissions?.includes(
                        "ORGANIZATION_REACTIVATE",
                      ) && (
                        <button
                          onClick={() => openRestoreModal(org.id)}
                          className="text-orange-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                        >
                          <ArrowPathIcon className="w-5 h-5" title="Restore" />
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
          {organizations.map((organization) => (
            <div
              key={organization.id}
              className="border rounded-xl p-4 bg-white"
            >
              <div className="flex justify-between">
                <h3 className="font-bold">{organization.organizationName}</h3>

                <span
                  className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                    organization.isActive,
                  )}`}
                >
                  {organization.isActive ? "ACTIVE" : "INACTIVE"}
                </span>
              </div>

              <div className="mt-3 space-y-1 text-sm">
                <p>
                  <b>Owner:</b> {organization.ownerName}
                </p>

                <p>
                  <b>City:</b> {organization.city}
                </p>

                <p>
                  <b>Country:</b> {organization.country}
                </p>

                <p>
                  <b>Plan:</b> {organization.subscriptionModel?.name || "N/A"}
                </p>

                <p>
                  <b>Billing:</b> {organization.billingCycle}
                </p>
              </div>

              <div className="flex gap-2 mt-4">
                {user?.permissions?.includes("ORGANIZATION_VIEW") && (
                  <button
                    className="flex-1 bg-green-500 text-white py-2 rounded-lg"
                    onClick={() => {
                      setModalMode("view");
                      setSelectedOrganization(organization);
                      setShowModal(true);
                    }}
                  >
                    View
                  </button>
                )}

                {user?.permissions?.includes("ORGANIZATION_UPDATE") && (
                  <button
                    className="flex-1 bg-blue-500 text-white py-2 rounded-lg"
                    onClick={() => {
                      setModalMode("edit");
                      setSelectedOrganization(organization);
                      setShowModal(true);
                    }}
                  >
                    Edit
                  </button>
                )}

                {organization.isActive &&
                  user.permissions.includes("ORGANIZATION_DELETE") && (
                    <button
                      className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                      onClick={() => openDeleteModal(organization.id)}
                    >
                      Delete
                    </button>
                  )}

                {!organization.isActive &&
                  user?.permissions?.includes("ORGANIZATION_REACTIVATE") && (
                    <button
                      className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                      onClick={() => openRestoreModal(organization.id)}
                    >
                      Restore
                    </button>
                  )}
              </div>
            </div>
          ))}
        </div>

        {/* ================= Pagination ================= */}

        <div className="mt-6 flex flex-col gap-4 border-t border-gray-200 pt-5 md:flex-row md:items-center md:justify-between">
          {/* Left */}

          <div className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-green-600">
              {organizations.length === 0 ? 0 : currentPage * pageSize + 1}
            </span>{" "}
            to{" "}
            <span className="font-semibold text-green-600">
              {Math.min(
                (currentPage + 1) * pageSize,
                currentPage * pageSize + organizations.length,
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

        {/* MODAL */}
        <OrganizationModalBox
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          mode={modalMode}
          organization={selectedOrganization}
          onSuccess={loadOrganizations}
        />
      </div>
    </div>
  );
}
