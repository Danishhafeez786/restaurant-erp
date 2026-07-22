import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import BranchModalBox from "./BranchModalBox";
import { PlusIcon } from "@heroicons/react/24/outline";

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
    branchName: "",
    branchCode: "",
    city: "",
    phone: "",
    organizationId: "",
    isActive: "",
  });

  const loadBranches = async () => {

    try {

      setLoading(true);

      const payload = {
        branchName: searchCriteria.branchName || null,
        branchCode: searchCriteria.branchCode || null,
        city: searchCriteria.city || null,
        phone: searchCriteria.phone || null,
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
      "http://localhost:8080/api/branch/stream"
    );

    eventSource.addEventListener("branch-created", loadBranches);
    eventSource.addEventListener("branch-updated", loadBranches);
    eventSource.addEventListener("branch-deleted", loadBranches);
    eventSource.addEventListener("branch-restored", loadBranches);

    return () => eventSource.close();

  }, []);

  const handleDelete = async (id) => {

    if (!window.confirm("Delete this branch?")) return;

    try {

      await axiosClient.delete(`/branch/${id}`);

      loadBranches();

    } catch (error) {

      console.error(error);

    }

  };

  const handleRestore = async (id) => {

    if (!window.confirm("Restore this branch?")) return;

    try {

      await axiosClient.patch(`/branch/${id}/restore`);

      loadBranches();

    } catch (error) {

      console.error(error);

    }

  };

  const resetFilters = () => {

    setSearchCriteria({
      branchName: "",
      branchCode: "",
      city: "",
      phone: "",
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

          <input
            type="text"
            placeholder="Branch Name"
            value={searchCriteria.branchName}
            onChange={(e) =>
              setSearchCriteria((prev) => ({
                ...prev,
                branchName: e.target.value,
              }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
          />

          <input
            type="text"
            placeholder="Branch Code"
            value={searchCriteria.branchCode}
            onChange={(e) =>
              setSearchCriteria((prev) => ({
                ...prev,
                branchCode: e.target.value,
              }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
          />

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

          <input
            type="text"
            placeholder="Phone"
            value={searchCriteria.phone}
            onChange={(e) =>
              setSearchCriteria((prev) => ({
                ...prev,
                phone: e.target.value,
              }))
            }
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
          />

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

          <select
            value={direction}
            onChange={(e) => setDirection(e.target.value)}
            className="h-11 w-full rounded-lg border px-4 focus:border-[#0d4039] focus:outline-none"
          >
            <option value="DESC">Newest</option>
            <option value="ASC">Oldest</option>
          </select>

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
                          onClick={() => handleDelete(branch.id)}
                        >
                          Delete
                        </button>
                      )
                    ) : (
                      user?.permissions?.includes("BRANCH_REACTIVATE") && (
                        <button
                          className="text-green-600"
                          onClick={() => handleRestore(branch.id)}
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
                    onClick={() => handleDelete(branch.id)}
                  >
                    Delete
                  </button>
                ) : (
                  user.permissions.includes("BRANCH_REACTIVATE") &&
                  <button
                    className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                    disabled={!user?.permissions?.includes("BRANCH_REACTIVATE")}
                    onClick={() => handleRestore(branch.id)}
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