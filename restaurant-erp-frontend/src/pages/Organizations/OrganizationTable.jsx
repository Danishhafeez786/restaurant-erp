import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import { PlusIcon } from "@heroicons/react/24/outline";
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
            error?.response?.data?.message || "Failed to delete organization"
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
            error?.response?.data?.message || "Failed to restore organization"
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
      const eventSource = new EventSource(
        `${API_URL}/organization/stream`
      );
  
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
    <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
      {/* HEADER */}
      <div className="flex justify-between items-center mb-5">
        <h2 className="text-2xl font-bold">Organizations</h2>

        {user?.permissions?.includes("ORGANIZATION_CREATE") && <button
          onClick={() => {
            setModalMode("create");
            setSelectedOrganization(null);
            setShowModal(true);
          }}
          className="px-3 sm:px-6 py-2 bg-[#0d4039] text-white rounded-lg font-medium flex items-center justify-center gap-2"
        >
          <PlusIcon className="w-5 h-5" title="Add Organization" />
          <span className="hidden sm:inline">Add Organization</span>
        </button>}
      </div>

      {/* FILTERS */}
      {/* SEARCH FILTERS */}

      {/* Search Toolbar */}
      <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5">

          {/* Search */}
          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Search
            </label>
            <input
              type="text"
              placeholder="Organization, Owner, City, Country..."
              value={searchCriteria.searchInput}
              onChange={(e) =>
                setSearchCriteria((prev) => ({
                  ...prev,
                  searchInput: e.target.value,
                }))
              }
              className="h-11 w-full rounded-lg border border-gray-300 px-4 focus:border-[#0d4039] focus:outline-none focus:ring-2 focus:ring-[#0d4039]/20"
            />
          </div>

          {/* Status */}
          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Status
            </label>
            <select
              value={searchCriteria.isActive}
              onChange={(e) =>
                setSearchCriteria((prev) => ({
                  ...prev,
                  isActive: e.target.value,
                }))
              }
              className="h-11 w-full rounded-lg border border-gray-300 px-4 focus:border-[#0d4039] focus:outline-none focus:ring-2 focus:ring-[#0d4039]/20"
            >
              <option value="">All Status</option>
              <option value="true">Active</option>
              <option value="false">Inactive</option>
            </select>
          </div>

          {/* Sort By */}
          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Sort By
            </label>
            <select
              value={sortBy}
              onChange={(e) => {
                setCurrentPage(0);
                setSortBy(e.target.value);
              }}
              className="h-11 w-full rounded-lg border border-gray-300 px-4 focus:border-[#0d4039] focus:outline-none focus:ring-2 focus:ring-[#0d4039]/20"
            >
              <option value="createdAt">Created Date</option>
              <option value="organizationName">Organization</option>
              <option value="ownerName">Owner</option>
              <option value="city">City</option>
              <option value="country">Country</option>
            </select>
          </div>

          {/* Sort Order */}
          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Order
            </label>
            <select
              value={direction}
              onChange={(e) => {
                setCurrentPage(0);
                setDirection(e.target.value);
              }}
              className="h-11 w-full rounded-lg border border-gray-300 px-4 focus:border-[#0d4039] focus:outline-none focus:ring-2 focus:ring-[#0d4039]/20"
            >
              <option value="DESC">Newest First</option>
              <option value="ASC">Oldest First</option>
            </select>
          </div>

          {/* Records Per Page */}
          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Records Per Page
            </label>
            <select
              value={pageSize}
              onChange={(e) => {
                setCurrentPage(0);
                setPageSize(Number(e.target.value));
              }}
              className="h-11 w-full rounded-lg border border-gray-300 px-4 focus:border-[#0d4039] focus:outline-none focus:ring-2 focus:ring-[#0d4039]/20"
            >
              <option value={10}>10</option>
              <option value={25}>25</option>
              <option value={50}>50</option>
              <option value={100}>100</option>
            </select>
          </div>

        </div>
      </div>

      {/* TABLE */}
      <div className="hidden md:block overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-b text-left">
              <th className="py-3">Organization</th>
              <th>Owner</th>
              <th>City</th>
              <th>Country</th>
              <th>Plan</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {organizations.map((org) => (
              <tr key={org.id} className="border-b hover:bg-gray-50">
                <td className="py-3">
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

                <td>{org.ownerName}</td>

                <td>{org.city}</td>

                <td>{org.country}</td>

                <td>{org.subscriptionModel?.name || "N/A"}</td>

                <td>
                  <span
                    className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                      org.isActive,
                    )}`}
                  >
                    {org.isActive ? "ACTIVE" : "INACTIVE"}
                  </span>
                </td>

                <td>
                  {user?.permissions?.includes("ORGANIZATION_VIEW") && <button
                    onClick={() => {
                      setModalMode("view");
                      setSelectedOrganization(org);
                      setShowModal(true);
                    }}
                    className="text-green-600 mr-3"
                  >
                    View
                  </button>}
                  {user?.permissions?.includes("ORGANIZATION_UPDATE") && <button
                    onClick={() => {
                      setModalMode("edit");
                      setSelectedOrganization(org);
                      setShowModal(true);
                    }}
                    className="text-blue-600 mr-3"
                  >
                    Edit
                  </button>}

                  {org.isActive && user?.permissions?.includes("ORGANIZATION_DELETE") && (
                    <button
                      onClick={() => openDeleteModal(org.id)}
                      className="text-red-600"
                    >
                      Delete
                    </button>
                  )}

                  {!org.isActive && user?.permissions?.includes("ORGANIZATION_REACTIVATE") && (
                    <button
                      onClick={() => openRestoreModal(org.id)}
                      className="text-orange-600"
                    >
                      Restore
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
          <div key={organization.id} className="border rounded-xl p-4 bg-white">
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
              {user?.permissions?.includes("ORGANIZATION_VIEW") && <button
                className="flex-1 bg-green-500 text-white py-2 rounded-lg"
                onClick={() => {
                  setModalMode("view");
                  setSelectedOrganization(organization);
                  setShowModal(true);
                }}
              >
                View
              </button>}

              {user?.permissions?.includes("ORGANIZATION_UPDATE") && <button
                className="flex-1 bg-blue-500 text-white py-2 rounded-lg"
                onClick={() => {
                  setModalMode("edit");
                  setSelectedOrganization(organization);
                  setShowModal(true);
                }}
              >
                Edit
              </button>}

              {organization.isActive && user.permissions.includes("ORGANIZATION_DELETE") && (
                <button
                  className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                  onClick={() => openDeleteModal(organization.id)}
                >
                  Delete
                </button>
              )}

              {!organization.isActive && user?.permissions?.includes("ORGANIZATION_REACTIVATE") && (
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

      {confirmModal.open && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl shadow-lg w-[90%] max-w-md p-6">

            <h2 className="text-xl font-bold text-gray-800">
              {confirmModal.title}
            </h2>

            <p className="mt-3 text-gray-600">
              {confirmModal.message}
            </p>

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
  );
}
