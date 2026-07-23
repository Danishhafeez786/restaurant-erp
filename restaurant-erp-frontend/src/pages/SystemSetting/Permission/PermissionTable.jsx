import { useEffect, useState } from "react";
import axiosClient from "../../../api/axiosClient";
import PermissionModalBox from "./PermissionModalBox";
import { PlusIcon } from "@heroicons/react/24/outline";

export default function PermissionTable() {
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
    code: "",
    name: "",
    module: "",
    isActive: "",
  });

  // ===== LOAD =====
  const loadPermissions = async () => {
    try {
      setLoading(true);

      const payload = {
        code: searchCriteria.code || null,
        name: searchCriteria.name || null,
        module: searchCriteria.module || null,
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
    const eventSource = new EventSource(
      "http://localhost:8080/api/permission/stream",
    );

    const refresh = () => loadPermissions();

    eventSource.addEventListener("permission-created", refresh);
    eventSource.addEventListener("permission-updated", refresh);
    eventSource.addEventListener("permission-deleted", refresh);
    eventSource.addEventListener("permission-restored", refresh);

    return () => {
      eventSource.close();
    };
  }, []);

  // ===== DELETE (SOFT DELETE FIX) =====
  const handleDelete = async (id) => {
    if (!window.confirm("Delete this permission?")) return;

    try {
      await axiosClient.delete(`/permission/${id}`);
      loadPermissions();
    } catch (error) {
      console.error(error);
    }
  };

  const handleRestore = async (id) => {
    if (!window.confirm("Restore this permission?")) return;

    try {
      await axiosClient.patch(`/permission/${id}/restore`);
      loadPermissions();
    } catch (error) {
      console.error(error);
    }
  };

  const getStatusColor = (status) =>
    status ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700";

  const pages = Array.from({ length: totalPages }, (_, i) => i);

  return (
    <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
      {/* HEADER */}
      <div className="flex justify-between items-center mb-5">
        <h2 className="text-2xl font-bold">Permissions</h2>

        <button
          onClick={() => {
            setModalMode("create");
            setSelectedPermission(null);
            setShowModal(true);
          }}
          className="px-3 sm:px-6 py-2 bg-[#0d4039] text-white rounded-lg font-medium flex items-center justify-center gap-2"
        >
          <PlusIcon className="w-5 h-5" title="Add Permission" />
          <span className="hidden sm:inline">Add Permission</span>
        </button>
      </div>

      {/* FILTERS */}
      <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">
  <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5">

    {/* Code */}
    <input
      type="text"
      placeholder="Code"
      value={searchCriteria.code}
      onChange={(e) =>
        setSearchCriteria((prev) => ({
          ...prev,
          code: e.target.value,
        }))
      }
      className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
    />

    {/* Name */}
    <input
      type="text"
      placeholder="Name"
      value={searchCriteria.name}
      onChange={(e) =>
        setSearchCriteria((prev) => ({
          ...prev,
          name: e.target.value,
        }))
      }
      className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
    />

    {/* Module */}
    <input
      type="text"
      placeholder="Module"
      value={searchCriteria.module}
      onChange={(e) =>
        setSearchCriteria((prev) => ({
          ...prev,
          module: e.target.value,
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
      <option value="code">Code</option>
      <option value="name">Name</option>
      <option value="module">Module</option>
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
        {loading ? (
          <div className="text-center py-10">Loading...</div>
        ) : (
          <table className="w-full border-separate border-spacing-y-2">
            <thead>
              <tr className="border-b text-left">
                <th>Code</th>
                <th>Name</th>
                <th>Module</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {permissions.map((p) => (
                <tr key={p.id} className="border-b hover:bg-gray-50">
                  <td>{p.code}</td>
                  <td>{p.name}</td>
                  <td>{p.module}</td>

                  <td>
                    <span
                      className={`px-3 py-1 rounded-full text-sm ${getStatusColor(p.isActive)}`}
                    >
                      {p.isActive ? "ACTIVE" : "INACTIVE"}
                    </span>
                  </td>

                  <td>
                    <button
                      onClick={() => {
                        setModalMode("view");
                        setSelectedPermission(p);
                        setShowModal(true);
                      }}
                      className="text-green-600 mr-3"
                    >
                      View
                    </button>

                    <button
                      onClick={() => {
                        setModalMode("edit");
                        setSelectedPermission(p);
                        setShowModal(true);
                      }}
                      className="text-blue-600 mr-3"
                    >
                      Edit
                    </button>

                    {p.isActive ? (
                      <button
                        onClick={() => handleDelete(p.id)}
                        className="text-red-600"
                      >
                        Delete
                      </button>
                    ) : (
                      <button
                        onClick={() => handleRestore(p.id)}
                        className="text-green-600"
                      >
                        Restore
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
      <div className="md:hidden space-y-4">
        {loading ? (
          <div className="text-center py-10 bg-white rounded-xl border">
            Loading...
          </div>
        ) : (
          permissions.map((permission) => (
            <div
              key={permission.id}
              className="border rounded-xl p-4 bg-white"
            >
              <div className="flex justify-between items-start">
                <h3 className="font-bold text-lg">
                  {permission.name}
                </h3>

                <span
                  className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                    permission.isActive
                  )}`}
                >
                  {permission.isActive ? "ACTIVE" : "INACTIVE"}
                </span>
              </div>

              <div className="mt-3 space-y-2 text-sm">
                <p>
                  <b>Code:</b> {permission.code}
                </p>

                <p>
                  <b>Module:</b> {permission.module}
                </p>
              </div>

              <div className="flex gap-2 mt-4">
                <button
                  className="flex-1 bg-green-500 text-white py-2 rounded-lg"
                  onClick={() => {
                    setModalMode("view");
                    setSelectedPermission(permission);
                    setShowModal(true);
                  }}
                >
                  View
                </button>

                {permission.isActive ? (
                  <>
                    <button
                      className="flex-1 bg-blue-500 text-white py-2 rounded-lg"
                      onClick={() => {
                        setModalMode("edit");
                        setSelectedPermission(permission);
                        setShowModal(true);
                      }}
                    >
                      Edit
                    </button>

                    <button
                      className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                      onClick={() => handleDelete(permission.id)}
                    >
                      Delete
                    </button>
                  </>
                ) : (
                  <button
                    className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                    onClick={() => handleRestore(permission.id)}
                  >
                    Restore
                  </button>
                )}
              </div>
            </div>
          ))
        )}
      </div>


      {/* PAGINATION */}
      <div className="flex justify-center gap-2 mt-5">
        {pages.map((i) => (
          <button
            key={i}
            onClick={() => setCurrentPage(i)}
            className={`px-3 py-1 rounded ${currentPage === i ? "bg-[#0d4039] text-white" : "bg-gray-200"
              }`}
          >
            {i + 1}
          </button>
        ))}
      </div>

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
  );
}
