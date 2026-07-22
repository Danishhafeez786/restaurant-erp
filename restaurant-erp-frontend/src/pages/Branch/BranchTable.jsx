import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import BranchModalBox from "./BranchModalBox";
import { PlusIcon } from "@heroicons/react/24/outline";
const API_URL = import.meta.env.VITE_API_URL;
import { toast } from "react-toastify";

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
            error?.response?.data?.message || "Failed to delete branch"
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
            error?.response?.data?.message || "Failed to restore branch"
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
        organizationId: searchCriteria.organizationId || null,
        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
      };

      const response = await axiosClient.post(
        `/branch/search?page=${currentPage}&size=${pageSize}&sortBy=${sortBy}&direction=${direction}`,
        payload
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
        const eventSource = new EventSource(
          `${API_URL}/branch/stream`
        );
    
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
    status
      ? "bg-green-100 text-green-700"
      : "bg-red-100 text-red-700";

  return (

    <div className="bg-white rounded-2xl shadow-md p-6">

      {/* Header */}

      <div className="flex items-center justify-between mb-6">

        <h2 className="text-2xl font-bold">

          Branches

        </h2>

        {user.permissions.includes("BRANCH_CREATE") && <button
          onClick={() => {
            setModalMode("create");
            setSelectedBranch(null);
            setShowModal(true);
          }}
          className="px-3 sm:px-6 py-2 bg-[#0d4039] text-white rounded-lg font-medium flex items-center justify-center gap-2"
        >
          <PlusIcon className="w-5 h-5" title="Add Branch" />
          <span className="hidden sm:inline">Add Branch</span>
        </button>}

      </div>
      {/* Filters */}

      <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5">

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Search
            </label>
            <input
              type="text"
              placeholder="Search..."
              value={searchCriteria.searchInput}
              onChange={(e) =>
                setSearchCriteria((prev) => ({
                  ...prev,
                  searchInput: e.target.value,
                }))
              }
              className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
            />
          </div>

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
              className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
            >
              <option value="">Status</option>
              <option value="true">Active</option>
              <option value="false">Inactive</option>
            </select>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Sort By
            </label>
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
            >
              <option value="createdAt">Created</option>
              <option value="branchName">Branch Name</option>
              <option value="branchCode">Branch Code</option>
              <option value="city">City</option>
            </select>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Order
            </label>
            <select
              value={direction}
              onChange={(e) => setDirection(e.target.value)}
              className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
            >
              <option value="DESC">Newest</option>
              <option value="ASC">Oldest</option>
            </select>
          </div>

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
              className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
            >
              <option value={10}>10</option>
              <option value={25}>25</option>
              <option value={50}>50</option>
              <option value={100}>100</option>
            </select>
          </div>

        </div>
      </div>

      {/* Table */}

      <div className="overflow-x-auto hidden md:block">

        <table className="w-full">

          <thead>

            <tr className="border-b">

              <th className="text-left py-3">Branch Name</th>
              <th className="text-left">Branch Code</th>
              <th className="text-left">Address</th>
              <th className="text-left">City</th>
              <th className="text-left">Phone</th>
              <th className="text-left">Organization</th>
              <th className="text-left">Status</th>
              <th className="text-left">Actions</th>

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

                <tr
                  key={branch.id}
                  className="border-b hover:bg-gray-50"
                >

                  <td className="py-3">{branch.branchName}</td>

                  <td>{branch.branchCode}</td>

                  <td>{branch.address}</td>

                  <td>{branch.city}</td>

                  <td>{branch.phone}</td>

                  <td>

                    {branch.organizationModel?.organizationName || "-"}

                  </td>

                  <td>

                    <span
                      className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                        branch.isActive
                      )}`}
                    >
                      {branch.isActive ? "ACTIVE" : "INACTIVE"}
                    </span>

                  </td>

                  <td className="space-x-3">

                    {user.permissions.includes("BRANCH_VIEW") && <button
                      className="text-green-600"
                      disabled={!user.permissions.includes("BRANCH_VIEW")}
                      onClick={() => {
                        setSelectedBranch(branch);
                        setModalMode("view");
                        setShowModal(true);
                      }}
                    >
                      View
                    </button>}

                    {user.permissions.includes("BRANCH_UPDATE") && <button
                      className="text-blue-600"
                      onClick={() => {
                        setSelectedBranch(branch);
                        setModalMode("edit");
                        setShowModal(true);
                      }}
                    >
                      Edit
                    </button>}

                    {branch.isActive ? (
                      user?.permissions?.includes("BRANCH_DELETE") && (
                        <button
                          className="text-red-600"
                          onClick={() => openDeleteModal(branch.id)}
                        >
                          Delete
                        </button>
                      )
                    ) : (
                      user?.permissions?.includes("BRANCH_REACTIVATE") && (
                        <button
                          className="text-green-600"
                          onClick={() => openRestoreModal(branch.id)}
                        >
                          Restore
                        </button>
                      )
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
            <div
              key={branch.id}
              className="border rounded-xl p-4 bg-white"
            >
              <div className="flex justify-between items-start">
                <h3 className="font-bold text-lg">
                  {branch.branchName}
                </h3>

                <span
                  className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                    branch.isActive
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
                {user.permissions.includes("BRANCH_VIEW") && <button
                  className="flex-1 bg-green-500 text-white py-2 rounded-lg"
                  onClick={() => {
                    setSelectedBranch(branch);
                    setModalMode("view");
                    setShowModal(true);
                  }}
                >
                  View
                </button>}

                {user.permissions.includes("BRANCH_UPDATE") && <button
                  className="flex-1 bg-blue-500 text-white py-2 rounded-lg"
                  onClick={() => {
                    setSelectedBranch(branch);
                    setModalMode("edit");
                    setShowModal(true);
                  }}
                >
                  Edit
                </button>}
                {branch.isActive ? (
                  user.permissions.includes("BRANCH_DELETE") &&
                  <button
                    className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                    onClick={() => openDeleteModal(branch.id)}
                  >
                    Delete
                  </button>
                ) : (
                  user.permissions.includes("BRANCH_REACTIVATE") &&
                  <button
                    className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                    disabled={!user?.permissions?.includes("BRANCH_REACTIVATE")}
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


      {/* Pagination */}

      <div className="flex justify-center gap-2 mt-5">
        {[...Array(totalPages)].map((_, index) => (
          <button
            key={index}
            onClick={() => setCurrentPage(index)}
            className={`px-4 py-2 rounded ${currentPage === index ? "bg-[#0d4039] text-white" : "bg-gray-200"
              }`}
          >
            {index + 1}
          </button>
        ))}
      </div>

      {/* {Model Box} */}
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

      {/* Modal */}

      <BranchModalBox
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        mode={modalMode}
        branch={selectedBranch}
        onSuccess={loadBranches}
      />

    </div>

  );

}