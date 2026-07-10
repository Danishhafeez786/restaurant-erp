import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

import RoleModalBox from "./RoleModalBox";

export default function RoleTable() {
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");

  const [selectedRole, setSelectedRole] = useState(null);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [pageSize, setPageSize] = useState(10);

  const [sortBy, setSortBy] = useState("createdAt");

  const [direction, setDirection] = useState("DESC");

  const [searchCriteria, setSearchCriteria] = useState({

    roleName: "",
    description: "",
    organization: "",
    isActive: "",

  });

    const user = JSON.parse(localStorage.getItem("user"));

    const canCreate = user?.permissions?.includes("ROLE_CREATE");

    const canView = user?.permissions?.includes("ROLE_VIEW");

    const canUpdate = user?.permissions?.includes("ROLE_UPDATE");

    const canDelete = user?.permissions?.includes("ROLE_DELETE");

    const canRestore = user?.permissions?.includes("ROLE_REACTIVATE");

  // ===== LOAD DATA =====

  const loadRoles = async () => {

    try {
      setLoading(true);
      const payload = {
        roleName: searchCriteria.roleName || null,
        description: searchCriteria.description || null,
        organization: searchCriteria.organization || null,
        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
      };

      const response = await axiosClient.post(
        `/role/search?page=${currentPage}&size=${pageSize}&sortBy=${sortBy}&direction=${direction}`,
        payload,
      );

      const pageData = response.data.data;
      setRoles(pageData.content);
      setTotalPages(pageData.totalPages);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  // ===== INIT LOAD =====

  useEffect(() => {
    loadRoles();
  }, [currentPage, pageSize, sortBy, direction]);

  useEffect(() => {
    const eventSource = new EventSource(
      "http://localhost:8080/api/role/stream",
    );
    return () => eventSource.close();
  }, []);

  // ===== DELETE =====

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this role?")) return;
    try {
      await axiosClient.delete(`/role/${id}`);
      alert("Role Deleted Successfully");
      loadRoles();
    } catch (error) {
      console.error(error);
    }
  };

  // ===== RESTORE =====

  const handleRestore = async (id) => {
    if (!window.confirm("Restore this role?")) return;
    try {
      await axiosClient.patch(`/role/${id}/restore`);
      alert("Role Restored Successfully");
      loadRoles();
    } catch (error) {
      console.error(error);
    }
  };

  const getStatusColor = (status) =>
    status
      ? "bg-green-100 text-green-700"
      : "bg-red-100 text-red-700";
  return (
    <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">

      {/* HEADER */}
      <div className="flex justify-between items-center mb-5">
        <h2 className="text-2xl font-bold">
          Roles
        </h2>
        <button
          onClick={() => {
            setModalMode("create");
            setSelectedRole(null);
            setShowModal(true);
          }}

          className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
        >
          + Add Role
        </button>
      </div>

      {/* FILTERS */}

      <div className="bg-white border rounded-xl shadow-sm p-4 mb-6">
        <div className="flex flex-wrap items-center gap-3">
          <input
            type="text"
            placeholder="Role Name"
            value={searchCriteria.roleName}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                roleName: e.target.value
              })
            }

            className="h-10 w-52 rounded-lg border px-3 text-sm"
          />

          <input
            type="text"
            placeholder="Description"
            value={searchCriteria.description}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                description: e.target.value
              })
            }

            className="h-10 w-52 rounded-lg border px-3 text-sm"
          />

          <input
            type="text"
            placeholder="Organization"
            value={searchCriteria.organization}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                organization: e.target.value
              })
            }
            className="h-10 w-44 rounded-lg border px-3 text-sm"
          />

          <select
            value={searchCriteria.isActive}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                isActive: e.target.value
              })
            }
            className="h-10 rounded-lg border px-3 text-sm"
          >
            <option value="">Status</option>
            <option value="true">Active</option>
            <option value="false">Inactive</option>
          </select>

          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="h-10 rounded-lg border px-3 text-sm"
          >
            <option value="createdAt">
              Created
            </option>
            <option value="roleName">
              Role Name
            </option>
          </select>
          <select
            value={direction}
            onChange={(e) => setDirection(e.target.value)}
            className="h-10 rounded-lg border px-3 text-sm"
          >
            <option value="DESC">
              Newest
            </option>
            <option value="ASC">
              Oldest
            </option>
          </select>
          <select
            value={pageSize}
            onChange={(e) => {
              setPageSize(Number(e.target.value));
              setCurrentPage(0);
            }}
            className="h-10 rounded-lg border px-3 text-sm"
          >
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
            <option value={100}>100</option>
          </select>
          <button
            onClick={() => {
              setCurrentPage(0);
              loadRoles();
            }}
            className="h-10 px-5 rounded-lg bg-[#0d4039] text-white"
          >
            Search
          </button>

          {/* Reset */}
          <button
            onClick={() => {
              setSearchCriteria({
                roleName: "",
                description: "",
                organization: "",
                isActive: "",
              });

              setSortBy("createdAt");
              setDirection("DESC");
              setPageSize(10);
              setCurrentPage(0);

              loadOrganizations();
            }}
            className="h-10 px-5 rounded-lg border hover:bg-gray-100 transition"
          >
            Reset
          </button>
        </div>
      </div>

      {/* TABLE */}
      <div className="hidden md:block overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-b text-left">
              <th className="py-3">Role Name</th>
              <th className="py-3">Description</th>
              <th className="py-3">Organization</th>
              <th className="py-3">Status</th>
              <th className="py-3">Action</th>
            </tr>
          </thead>
          <tbody>
            {roles.map((role) => (
              <tr key={role.id} className="border-b hover:bg-gray-50">
                <td className="py-3">
                  {role.roleName}
                </td>
                <td className="py-3">
                  {role.description}
                </td>
                <td className="py-3">
                  {role.organizationModel?.organizationName || "N/A"}
                </td>
                <td className="py-3">
                  <span
                    className={`px-3 py-1 rounded-full text-sm ${getStatusColor(role.isActive)}`}
                  >
                    {role.isActive ? "ACTIVE" : "INACTIVE"}
                  </span>
                </td>
                <td className="py-3">

                  {canView && (
                  <button
                    onClick={() => {
                      setModalMode("view");
                      setSelectedRole(role);
                      setShowModal(true);
                  }}
                    className="text-green-600 mr-3"
                  >
                    View
                  </button>
                  )}

                  {canUpdate && (
                  <button
                    onClick={() => {
                      setModalMode("edit");
                      setSelectedRole(role);
                      setShowModal(true);
                    }}
                    className="text-blue-600 mr-3"
                  >
                    Edit
                  </button>
                    )}

                  {role.isActive &&
                    canDelete && (
                  <button
                    onClick={() => handleDelete(role.id)}
                    className="text-red-600"
                  >
                    Delete
                  </button>
                    )}

                  {!role.isActive &&
                    canRestore && (
                  <button
                    className="text-orange-600"
                    onClick={() => handleRestore(role.id)}
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

      {/* MOBILE TABLE */}
      {/* MOBILE */}
      <div className="md:hidden space-y-4">
        {roles.map((role) => (
          <div
            key={role.id}
            className="border rounded-xl p-4 bg-white"
          >
            <div className="flex justify-between items-start">
              <h3 className="font-bold text-lg">
                {role.roleName}
              </h3>

              <span
                className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                  role.isActive
                )}`}
              >
                {role.isActive ? "ACTIVE" : "INACTIVE"}
              </span>
            </div>

            <div className="mt-3 space-y-2 text-sm">
              <p>
                <b>Description:</b>{" "}
                {role.description || "N/A"}
              </p>

              <p>
                <b>Organization:</b>{" "}
                {role.organizationModel?.organizationName || "N/A"}
              </p>
            </div>

            <div className="flex gap-2 mt-4">

                            {canView && (
                                <button
                                    className="flex-1 bg-green-500 text-white py-2 rounded-lg"
                                    onClick={() => {
                                        setModalMode("view");
                                        setSelectedRole(role);
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
                                        setSelectedRole(role);
                                        setShowModal(true);
                                    }}
                                >
                                    Edit
                                </button>
                            )}

                            {role.isActive &&
                                canDelete && (
                                    <button
                                        className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                                        onClick={() => handleDelete(role.id)}
                                    >
                                        Delete
                                    </button>
                                )}

                            {!role.isActive &&
                                canRestore && (
                                    <button
                                        className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                                        onClick={() => handleRestore(role.id)}
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


            className={`px-3 py-1 rounded ${currentPage === i
              ? "bg-green-600 text-white"
              : "bg-gray-200"
              }`}


          >


            {i + 1}


          </button>



        ))}



      </div>








      {/* MODAL */}



      <RoleModalBox


        isOpen={showModal}


        onClose={() => setShowModal(false)}


        mode={modalMode}


        role={selectedRole}


        onSuccess={loadRoles}


      />





    </div>


  );



}