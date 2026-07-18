import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

import OrganizationModalBox from "./OrganizationModalBox";

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
    organizationName: "",
    ownerName: "",
    city: "",
    country: "",
    isActive: "",
    billingCycle: "",
    subscriptionPlanId: "",
  });

  // ===== LOAD DATA =====
  const loadOrganizations = async () => {
    try {
      setLoading(true);

      const payload = {
        organizationName: searchCriteria.organizationName || null,
        ownerName: searchCriteria.ownerName || null,
        city: searchCriteria.city || null,
        country: searchCriteria.country || null,
        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
        billingCycle: searchCriteria.billingCycle || null,
        subscriptionPlanId: searchCriteria.subscriptionPlanId || null,
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
    console.log("Connecting to SSE...");

    const eventSource = new EventSource(
      "http://localhost:8080/api/organization/stream",
    );


    return () => eventSource.close();
  }, []);

  // ===== DELETE =====
  const handleDelete = async (id) => {
    if (!window.confirm("Delete this organization?")) return;

    try {
      await axiosClient.delete(`/organization/${id}`);

      alert("Organization Deleted Successfully");

      loadOrganizations();
    } catch (error) {
      console.error(error);
    }
  };

  // ===== RESTORE =====
  const handleRestore = async (id) => {
    if (!window.confirm("Restore this organization?")) return;

    try {
      await axiosClient.patch(`/organization/${id}/restore`);

      alert("Organization Restored Successfully");

      loadOrganizations();
    } catch (error) {
      console.error(error);
    }
  };

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
          className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
        >
          + Add Organization
        </button>}
      </div>

      {/* FILTERS */}
      {/* SEARCH FILTERS */}

      {/* Search Toolbar */}
      <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">
  <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5">

    {/* Organization */}
    <input
      type="text"
      placeholder="Organization"
      value={searchCriteria.organizationName}
      onChange={(e) =>
        setSearchCriteria((prev) => ({
          ...prev,
          organizationName: e.target.value,
        }))
      }
      className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
    />

    {/* Owner */}
    <input
      type="text"
      placeholder="Owner"
      value={searchCriteria.ownerName}
      onChange={(e) =>
        setSearchCriteria((prev) => ({
          ...prev,
          ownerName: e.target.value,
        }))
      }
      className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
    />

    {/* City */}
    <input
      type="text"
      placeholder="City"
      value={searchCriteria.city}
      onChange={(e) =>
        setSearchCriteria((prev) => ({
          ...prev,
          city: e.target.value,
        }))
      }
      className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
    />

    {/* Country */}
    <input
      type="text"
      placeholder="Country"
      value={searchCriteria.country}
      onChange={(e) =>
        setSearchCriteria((prev) => ({
          ...prev,
          country: e.target.value,
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

    {/* Billing */}
    <select
      value={searchCriteria.billingCycle}
      onChange={(e) =>
        setSearchCriteria((prev) => ({
          ...prev,
          billingCycle: e.target.value,
        }))
      }
      className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
    >
      <option value="">Billing</option>
      <option value="MONTHLY">Monthly</option>
      <option value="QUARTERLY">Quarterly</option>
      <option value="YEARLY">Yearly</option>
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
      <option value="organizationName">Organization</option>
      <option value="ownerName">Owner</option>
      <option value="city">City</option>
      <option value="country">Country</option>
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
                <td className="py-3">{org.organizationName}</td>

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
                        onClick={() => handleDelete(org.id)}
                        className="text-red-600"
                      >
                        Delete
                      </button>
                  )}

                  {!org.isActive && user?.permissions?.includes("ORGANIZATION_REACTIVATE") && (
                    <button
                      onClick={() => handleRestore(org.id)}
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

              {organization.isActive && user.permissions.includes("ORGANIZATION_DELETE") &&(
                <button
                  className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                  onClick={() => handleDelete(organization.id)}
                >
                  Delete
                </button>
              )}

              {!organization.isActive && user?.permissions?.includes("ORGANIZATION_REACTIVATE") && (
                <button
                  className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                  onClick={() => handleRestore(organization.id)}
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
