import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

import {
  connectOrganizationSocket,
  disconnectOrganizationSocket,
} from "../../services/websocket/organizationSocket";

import OrganizationModalBox from "./OrganizationModalBox";

export default function OrganizationTable() {
  const [organizations, setOrganizations] = useState([]);
  const [loading, setLoading] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");

  const [selectedOrganization, setSelectedOrganization] = useState(null);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

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
        `/organization/search?page=${currentPage}&size=10`,
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
  }, [currentPage]);

  // ===== REALTIME SOCKET =====
  useEffect(() => {
    connectOrganizationSocket((event) => {
      console.log("Organization Event:", event);
      loadOrganizations();
    });

    return () => {
      disconnectOrganizationSocket();
    };
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

        <button
          onClick={() => {
            setModalMode("create");
            setSelectedOrganization(null);
            setShowModal(true);
          }}
          className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
        >
          + Add Organization
        </button>
      </div>

      {/* FILTERS */}
      {/* SEARCH FILTERS */}

      <div className="bg-gray-50 border rounded-2xl p-4 md:p-5 mb-6">
        <h3 className="text-lg font-semibold mb-4">Search Organizations</h3>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {/* Organization Name */}
          <input
            type="text"
            placeholder="Organization Name"
            value={searchCriteria.organizationName}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                organizationName: e.target.value,
              })
            }
            className="border rounded-xl px-4 py-3 focus:ring-2 focus:ring-green-500 outline-none"
          />

          {/* Owner Name */}
          <input
            type="text"
            placeholder="Owner Name"
            value={searchCriteria.ownerName}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                ownerName: e.target.value,
              })
            }
            className="border rounded-xl px-4 py-3 focus:ring-2 focus:ring-green-500 outline-none"
          />

          {/* City */}
          <input
            type="text"
            placeholder="City"
            value={searchCriteria.city}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                city: e.target.value,
              })
            }
            className="border rounded-xl px-4 py-3 focus:ring-2 focus:ring-green-500 outline-none"
          />

          {/* Country */}
          <input
            type="text"
            placeholder="Country"
            value={searchCriteria.country}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                country: e.target.value,
              })
            }
            className="border rounded-xl px-4 py-3 focus:ring-2 focus:ring-green-500 outline-none"
          />

          {/* Status */}
          <select
            value={searchCriteria.isActive}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                isActive: e.target.value,
              })
            }
            className="border rounded-xl px-4 py-3 focus:ring-2 focus:ring-green-500 outline-none"
          >
            <option value="">All Status</option>
            <option value="true">Active</option>
            <option value="false">Inactive</option>
          </select>

          {/* Billing Cycle */}
          <select
            value={searchCriteria.billingCycle}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                billingCycle: e.target.value,
              })
            }
            className="border rounded-xl px-4 py-3 focus:ring-2 focus:ring-green-500 outline-none"
          >
            <option value="">All Billing Cycles</option>
            <option value="MONTHLY">Monthly</option>
            <option value="QUARTERLY">Quarterly</option>
            <option value="YEARLY">Yearly</option>
          </select>

          {/* Subscription Plan Id */}
          <input
            type="text"
            placeholder="Subscription Plan ID"
            value={searchCriteria.subscriptionPlanId}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                subscriptionPlanId: e.target.value,
              })
            }
            className="border rounded-xl px-4 py-3 focus:ring-2 focus:ring-green-500 outline-none"
          />

          {/* Buttons */}
          <div className="flex gap-2">
            <button
              onClick={() => {
                setCurrentPage(0);
                loadOrganizations();
              }}
              className="flex-1 bg-[#0d4039] hover:bg-[#145148] text-white rounded-xl py-3 font-medium"
            >
              Search
            </button>

            <button
              onClick={() => {
                setSearchCriteria({
                  organizationName: "",
                  ownerName: "",
                  city: "",
                  country: "",
                  isActive: "",
                  billingCycle: "",
                  subscriptionPlanId: "",
                });

                setCurrentPage(0);
              }}
              className="flex-1 bg-gray-200 hover:bg-gray-300 rounded-xl py-3 font-medium"
            >
              Reset
            </button>
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
                  <button
                    onClick={() => {
                      setModalMode("view");
                      setSelectedOrganization(org);
                      setShowModal(true);
                    }}
                    className="text-green-600 mr-3"
                  >
                    View
                  </button>

                  {org.isActive && (
                    <>
                      <button
                        onClick={() => {
                          setModalMode("edit");
                          setSelectedOrganization(org);
                          setShowModal(true);
                        }}
                        className="text-blue-600 mr-3"
                      >
                        Edit
                      </button>

                      <button
                        onClick={() => handleDelete(org.id)}
                        className="text-red-600"
                      >
                        Delete
                      </button>
                    </>
                  )}

                  {!org.isActive && (
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

              {organization.isActive && (
                <>
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

                  <button
                    className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                    onClick={() => handleDelete(organization.id)}
                  >
                    Delete
                  </button>
                </>
              )}

              {!organization.isActive && (
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
            className={`px-3 py-1 rounded ${currentPage === i ? "bg-green-600 text-white" : "bg-gray-200"}`}
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
