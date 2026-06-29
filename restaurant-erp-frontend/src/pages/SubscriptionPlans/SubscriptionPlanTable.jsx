import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosClient from "../../api/axiosClient";
import SubscriptionPlanModal from "./SubscriptionPlanModalBox";

export default function SubscriptionPlanTable() {
  const navigate = useNavigate();

  const [plans, setPlans] = useState([]);

  const [loading, setLoading] = useState(false);

  const [showModal, setShowModal] = useState(false);

  const [modalMode, setModalMode] = useState("create");

  const [selectedPlan, setSelectedPlan] = useState(null);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [pageSize, setPageSize] = useState(10);

  const [sortBy, setSortBy] = useState("createdAt");

  const [direction, setDirection] = useState("DESC");

  const [searchCriteria, setSearchCriteria] = useState({
    name: "",
    isActive: "",
    minMonthlyPrice: "",
    maxMonthlyPrice: "",
    minUsersLimit: "",
    maxUsersLimit: "",
  });

  useEffect(() => {
    loadPlans();
  }, [currentPage, pageSize, sortBy, direction]);

  useEffect(() => {
    console.log("Connecting to Subscription Plan SSE...");

    const eventSource = new EventSource(
      "http://localhost:8080/api/subscription_plans/stream",
    );

  }, []);

  const loadPlans = async () => {
    try {
      setLoading(true);

      const payload = {
        name: searchCriteria.name || null,
        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
        minMonthlyPrice: searchCriteria.minMonthlyPrice || null,
        maxMonthlyPrice: searchCriteria.maxMonthlyPrice || null,
        minUsersLimit: searchCriteria.minUsersLimit || null,
        maxUsersLimit: searchCriteria.maxUsersLimit || null,
      };

      const response = await axiosClient.post(
        `/subscription_plans/search?page=${currentPage}&size=${pageSize}&sortBy=${sortBy}&direction=${direction}`,
        payload,
      );

      const pageData = response.data.data;

      setPlans(pageData.content);
      setTotalPages(pageData.totalPages);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this subscription plan?",
    );

    if (!confirmed) return;

    try {
      await axiosClient.delete(`/subscription_plans/${id}`);

      alert("Subscription Plan Deleted Successfully");

      loadPlans();
    } catch (error) {
      console.error(error);

      alert(error?.response?.data?.message || "Failed to delete plan");
    }
  };

  const handleRestore = async (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to restore this subscription plan?",
    );

    if (!confirmed) return;

    try {
      alert("Restoring subscription plan...");
      await axiosClient.patch(`/subscription_plans/${id}/restore`);

      alert("Subscription Plan Restored Successfully");

      loadPlans();
    } catch (error) {
      console.error(error);

      alert(error?.response?.data?.message || "Failed to restore plan");
    }
  };

  const getStatusColor = (status) => {
    return status ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700";
  };

  return (
    <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
      {/* Header */}

      <div className="flex justify-between items-center mb-5">
        <h2 className="text-2xl font-bold">Subscription Plans</h2>

        <button
          onClick={() => {
            setModalMode("create");
            setSelectedPlan(null);
            setShowModal(true);
          }}
          className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
        >
          + Add Plan
        </button>
      </div>

      {/* Filters */}

      {/* SEARCH FILTERS */}

      {/* Search Toolbar */}
      <div className="bg-white border rounded-xl shadow-sm p-4 mb-6">
        <div className="flex flex-wrap items-center gap-3">
          {/* Plan Name */}
          <input
            type="text"
            placeholder="Plan Name"
            value={searchCriteria.name}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                name: e.target.value,
              })
            }
            className="h-10 w-52 rounded-lg border px-3 text-sm focus:border-[#0d4039] focus:ring-2 focus:ring-[#0d4039]/20 outline-none"
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
            className="h-10 rounded-lg border px-3 text-sm"
          >
            <option value="">Status</option>
            <option value="true">Active</option>
            <option value="false">Inactive</option>
          </select>

          {/* Min Monthly Price */}
          <input
            type="number"
            placeholder="Min Price"
            value={searchCriteria.minMonthlyPrice}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                minMonthlyPrice: e.target.value,
              })
            }
            className="h-10 w-36 rounded-lg border px-3 text-sm focus:border-[#0d4039] focus:ring-2 focus:ring-[#0d4039]/20 outline-none"
          />

          {/* Max Monthly Price */}
          <input
            type="number"
            placeholder="Max Price"
            value={searchCriteria.maxMonthlyPrice}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                maxMonthlyPrice: e.target.value,
              })
            }
            className="h-10 w-36 rounded-lg border px-3 text-sm focus:border-[#0d4039] focus:ring-2 focus:ring-[#0d4039]/20 outline-none"
          />

          {/* Min Users */}
          <input
            type="number"
            placeholder="Min Users"
            value={searchCriteria.minUsersLimit}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                minUsersLimit: e.target.value,
              })
            }
            className="h-10 w-32 rounded-lg border px-3 text-sm focus:border-[#0d4039] focus:ring-2 focus:ring-[#0d4039]/20 outline-none"
          />

          {/* Max Users */}
          <input
            type="number"
            placeholder="Max Users"
            value={searchCriteria.maxUsersLimit}
            onChange={(e) =>
              setSearchCriteria({
                ...searchCriteria,
                maxUsersLimit: e.target.value,
              })
            }
            className="h-10 w-32 rounded-lg border px-3 text-sm focus:border-[#0d4039] focus:ring-2 focus:ring-[#0d4039]/20 outline-none"
          />

          {/* Sort By */}
          <select
            value={sortBy}
            onChange={(e) => {
              setSortBy(e.target.value);
              setCurrentPage(0);
            }}
            className="h-10 rounded-lg border px-3 text-sm"
          >
            <option value="createdAt">Created</option>
            <option value="name">Plan Name</option>
            <option value="monthlyPrice">Monthly Price</option>
            <option value="yearlyPrice">Yearly Price</option>
            <option value="usersLimit">Users Limit</option>
            <option value="branchesLimit">Branches Limit</option>
          </select>

          {/* Sort Direction */}
          <select
            value={direction}
            onChange={(e) => {
              setDirection(e.target.value);
              setCurrentPage(0);
            }}
            className="h-10 rounded-lg border px-3 text-sm"
          >
            <option value="DESC">Newest</option>
            <option value="ASC">Oldest</option>
          </select>

          {/* Page Size */}
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

          {/* Search */}
          <button
            onClick={() => {
              setCurrentPage(0);
              loadPlans();
            }}
            className="h-10 px-5 rounded-lg bg-[#0d4039] text-white hover:bg-[#145148] transition"
          >
            Search
          </button>

          {/* Reset */}
          <button
            onClick={() => {
              setSearchCriteria({
                name: "",
                isActive: "",
                minMonthlyPrice: "",
                maxMonthlyPrice: "",
                minUsersLimit: "",
                maxUsersLimit: "",
              });

              setCurrentPage(0);
              setPageSize(10);
              setSortBy("createdAt");
              setDirection("DESC");

              loadPlans();
            }}
            className="h-10 px-5 rounded-lg border hover:bg-gray-100 transition"
          >
            Reset
          </button>
        </div>
      </div>

      {/* Desktop Table */}

      <div className="hidden md:block overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-b text-left">
              <th className="py-3">Name</th>
              <th>Monthly</th>
              <th>Yearly</th>
              <th>Branches</th>
              <th>Users</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {plans.map((plan) => (
              <tr key={plan.id} className="border-b hover:bg-gray-50">
                <td className="py-3">{plan.name}</td>

                <td>Rs. {plan.monthlyPrice}</td>

                <td>Rs. {plan.yearlyPrice}</td>

                <td>{plan.branchesLimit}</td>

                <td>{plan.usersLimit}</td>

                <td>
                  <span
                    className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                      plan.isActive,
                    )}`}
                  >
                    {plan.isActive ? "ACTIVE" : "INACTIVE"}
                  </span>
                </td>

                <td>
                  <button
                    onClick={() => {
                      setModalMode("view");
                      setSelectedPlan(plan);
                      setShowModal(true);
                    }}
                    className="text-green-600 mr-3"
                  >
                    View
                  </button>

                  {plan.isActive && (
                    <>
                      <button
                        onClick={() => {
                          setModalMode("edit");
                          setSelectedPlan(plan);
                          setShowModal(true);
                        }}
                        className="text-blue-600 mr-3"
                      >
                        Edit
                      </button>

                      <button
                        onClick={() => handleDelete(plan.id)}
                        className="text-red-600"
                      >
                        Delete
                      </button>
                    </>
                  )}

                  {!plan.isActive && (
                    <button
                      onClick={() => handleRestore(plan.id)}
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

      {/* Mobile Cards */}

      <div className="md:hidden space-y-4">
        {plans.map((plan) => (
          <div key={plan.id} className="border rounded-xl p-4">
            <div className="flex justify-between">
              <h3 className="font-bold">{plan.name}</h3>

              <span
                className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                  plan.isActive,
                )}`}
              >
                {plan.isActive ? "ACTIVE" : "INACTIVE"}
              </span>
            </div>

            <div className="mt-3 space-y-1 text-sm">
              <p>
                <b>Monthly:</b> Rs. {plan.monthlyPrice}
              </p>

              <p>
                <b>Yearly:</b> Rs. {plan.yearlyPrice}
              </p>

              <p>
                <b>Users:</b> {plan.usersLimit}
              </p>
            </div>

            <div className="flex gap-3 mt-4">
              <button
                className="flex-1 bg-green-500 text-white py-2 rounded-xl"
                onClick={() => {
                  setModalMode("view");
                  setSelectedPlan(plan);
                  setShowModal(true);
                }}
              >
                View
              </button>

              {plan.isActive && (
                <>
                  <button
                    className="flex-1 bg-blue-500 text-white py-2 rounded-xl"
                    onClick={() => {
                      setModalMode("edit");
                      setSelectedPlan(plan);
                      setShowModal(true);
                    }}
                  >
                    Edit
                  </button>

                  <button
                    className="flex-1 bg-red-500 text-white py-2 rounded-xl"
                    onClick={() => handleDelete(plan.id)}
                  >
                    Delete
                  </button>
                </>
              )}
              {!plan.isActive && (
                <button
                  className="flex-1 bg-orange-500 text-white py-2 rounded-xl"
                  onClick={() => handleRestore(plan.id)}
                >
                  Restore
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Pagination */}

      <div className="flex justify-center gap-2 mt-5">
        {[...Array(totalPages)].map((_, index) => (
          <button
            key={index}
            onClick={() => setCurrentPage(index)}
            className={`px-4 py-2 rounded ${
              currentPage === index ? "bg-[#0d4039] text-white" : "bg-gray-200"
            }`}
          >
            {index + 1}
          </button>
        ))}
      </div>

      {/* View Modal */}

      <SubscriptionPlanModal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        mode={modalMode}
        plan={selectedPlan}
        onSuccess={loadPlans}
      />
    </div>
  );
}
