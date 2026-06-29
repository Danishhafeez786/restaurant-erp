import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PermissionModalBox from "./PermissionModalBox";

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
          className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
        >
          + Add Permission
        </button>
      </div>

      {/* FILTERS */}
      <div className="border rounded-xl p-4 mb-6">
        <div className="flex flex-wrap gap-3">
          <input
            placeholder="Code"
            value={searchCriteria.code}
            onChange={(e) =>
              setSearchCriteria({ ...searchCriteria, code: e.target.value })
            }
            className="h-10 w-44 border rounded-lg px-3 text-sm"
          />

          <input
            placeholder="Name"
            value={searchCriteria.name}
            onChange={(e) =>
              setSearchCriteria({ ...searchCriteria, name: e.target.value })
            }
            className="h-10 w-52 border rounded-lg px-3 text-sm"
          />

          <input
            placeholder="Module"
            value={searchCriteria.module}
            onChange={(e) =>
              setSearchCriteria({ ...searchCriteria, module: e.target.value })
            }
            className="h-10 w-44 border rounded-lg px-3 text-sm"
          />

          <select
            value={searchCriteria.isActive}
            onChange={(e) =>
              setSearchCriteria({ ...searchCriteria, isActive: e.target.value })
            }
            className="h-10 border rounded-lg px-3 text-sm"
          >
            <option value="">Status</option>
            <option value="true">Active</option>
            <option value="false">Inactive</option>
          </select>

          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="h-10 border rounded-lg px-3 text-sm"
          >
            <option value="createdAt">Created</option>
            <option value="code">Code</option>
            <option value="name">Name</option>
            <option value="module">Module</option>
          </select>

          <select
            value={direction}
            onChange={(e) => setDirection(e.target.value)}
            className="h-10 border rounded-lg px-3 text-sm"
          >
            <option value="DESC">Newest</option>
            <option value="ASC">Oldest</option>
          </select>

          <button
            onClick={() => {
              setCurrentPage(0);
              loadPermissions();
            }}
            className="h-10 px-5 bg-[#0d4039] text-white rounded-lg"
          >
            Search
          </button>

          <button
            onClick={() => {
              setSearchCriteria({
                code: "",
                name: "",
                module: "",
                isActive: "",
              });
              setCurrentPage(0);
              loadPermissions();
            }}
            className="h-10 px-5 border rounded-lg"
          >
            Reset
          </button>
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

      {/* PAGINATION */}
      <div className="flex justify-center gap-2 mt-5">
        {pages.map((i) => (
          <button
            key={i}
            onClick={() => setCurrentPage(i)}
            className={`px-3 py-1 rounded ${
              currentPage === i ? "bg-green-600 text-white" : "bg-gray-200"
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
