import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosClient from "../../api/axiosClient";
import SubscriptionPlanModal from "./SubscriptionPlanModalBox";
import { PlusIcon, EyeIcon, PencilSquareIcon, TrashIcon, ArrowPathIcon,} from "@heroicons/react/24/outline";
import { toast } from "react-toastify";
const API_URL = import.meta.env.VITE_API_URL;

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

  const [searchCriteria, setSearchCriteria] = useState({ name: "", isActive: "" });

  const user = JSON.parse(localStorage.getItem("user"));

  const canCreate = user?.permissions?.includes("PLAN_CREATE");

  const canView = user?.permissions?.includes("PLAN_VIEW");

  const canUpdate = user?.permissions?.includes("PLAN_UPDATE");

  const canDelete = user?.permissions?.includes("PLAN_DELETE");

  const canRestore = user?.permissions?.includes("PLAN_REACTIVATE");

  const [confirmModal, setConfirmModal] = useState({
    open: false,
    title: "",
    message: "",
    action: null,
  });

  const openDeleteModal = (id) => {
    setConfirmModal({
      open: true,
      title: "Delete Subscription Plan",
      message: "Are you sure you want to delete this subscription plan?",
      action: async () => {
        try {
          await axiosClient.delete(`/subscription_plans/${id}`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to delete plan"
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
      title: "Restore Subscription Plan",
      message: "Are you sure you want to restore this subscription plan?",
      action: async () => {
        try {
          await axiosClient.patch(`/subscription_plans/${id}/restore`);
        } catch (error) {
          toast.error(
            error?.response?.data?.message || "Failed to restore plan"
          );
        } finally {
          setConfirmModal((prev) => ({ ...prev, open: false }));
        }
      },
    });
  };

  useEffect(() => {
    loadPlans();
  }, [currentPage, pageSize, sortBy, direction]);

  useEffect(() => {
    const timer = setTimeout(() => {
      setCurrentPage(0);
      loadPlans();
    }, 300);

    return () => clearTimeout(timer);
  }, [searchCriteria, sortBy, direction, pageSize]);

  useEffect(() => {
    const eventSource = new EventSource(
      `${API_URL}/subscription_plans/stream`
    );

    eventSource.addEventListener("subscription-created", () => {
      loadPlans();
      toast.success("A new subscription plan was created.");
    });

    eventSource.addEventListener("subscription-updated", () => {
      loadPlans();
      toast.success("A subscription plan was updated.");
    });

    eventSource.addEventListener("subscription-deleted", async (event) => {
      loadPlans();
      toast.success("A subscription plan was deleted.");
    });

    eventSource.addEventListener("subscription-restored", () => {
      loadPlans();
      toast.success("A subscription plan was restored.");
    });

    return () => eventSource.close();
  }, []);

  const loadPlans = async () => {
    try {
      setLoading(true);

      const payload = {
        name: searchCriteria.name || null,
        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true"
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

  const getStatusColor = (status) => {
    return status ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700";
  };

  return (
    <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
      {/* Header */}

      <div className="flex justify-between items-center mb-5">
        <h2 className="text-2xl font-bold">Subscription Plans</h2>

        {canCreate && <button
          onClick={() => {
            setModalMode("create");
            setSelectedPlan(null);
            setShowModal(true);
          }}
          className="px-3 sm:px-6 py-2 bg-[#0d4039] text-white rounded-lg font-medium flex items-center justify-center gap-2"
        >
          <PlusIcon className="w-5 h-5" />
          <span className="hidden sm:inline">Add Plan</span>
        </button>}
      </div>

      {/* Filters */}

      {/* SEARCH FILTERS */}

      {/* Search Toolbar */}
      <div className="mb-6 rounded-xl border bg-white p-4 shadow-sm">
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5">

          {/* Plan Name */}
          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Plan Name
            </label>
            <input
              type="text"
              placeholder="Enter plan name"
              value={searchCriteria.name}
              onChange={(e) =>
                setSearchCriteria((prev) => ({
                  ...prev,
                  name: e.target.value,
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
              <option value="name">Plan Name</option>
              <option value="monthlyPrice">Monthly Price</option>
              <option value="yearlyPrice">Yearly Price</option>
              <option value="usersLimit">Users Limit</option>
              <option value="branchesLimit">Branches Limit</option>
            </select>
          </div>

          {/* Sort Direction */}
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

          {/* Page Size */}
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

      {/* Desktop Table */}

      <div className="hidden md:block overflow-x-auto">
        <table className="w-full border-separate border-spacing-0 rounded-xl border border-gray-200 text-sm">
          <thead>
            <tr className="bg-gray-100">
              <th className="rounded-tl-xl px-4 py-3 text-left">Name</th>
              <th className="px-4 py-3 text-left">Monthly</th>
              <th className="px-4 py-3 text-left">Yearly</th>
              <th className="px-4 py-3 text-left">Branches</th>
              <th className="px-4 py-3 text-left">Users</th>
              <th className="px-4 py-3 text-left">Status</th>
              <th className="rounded-tr-xl px-4 py-3 text-left">Action</th>
            </tr>
          </thead>

          <tbody>
            {plans.map((plan) => (
              <tr key={plan.id} className="border-b hover:bg-gray-50">
                <td className="px-4 py-3 ">{plan.name}</td>

                <td className="px-4 py-3 ">Rs. {plan.monthlyPrice}</td>

                <td className="px-4 py-3 ">Rs. {plan.yearlyPrice}</td>

                <td className="px-4 py-3 ">{plan.branchesLimit}</td>

                <td className="px-4 py-3 ">{plan.usersLimit}</td>

                <td className="px-4 py-3 ">
                  <span
                    className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                      plan.isActive,
                    )}`}
                  >
                    {plan.isActive ? "ACTIVE" : "INACTIVE"}
                  </span>
                </td>

                <td className="px-4 py-3 ">

                  {canView && (
                    <button
                      onClick={() => {
                        setModalMode("view");
                        setSelectedPlan(plan);
                        setShowModal(true);
                      }}
                      className="text-green-600 mr-3"
                    >
                      <EyeIcon className="w-5 h-5" title="View" />
                    </button>
                  )}

                  {canUpdate && (
                    <button
                      onClick={() => {
                        setModalMode("edit");
                        setSelectedPlan(plan);
                        setShowModal(true);
                      }}
                      className="text-blue-600 mr-3"
                    >
                      <PencilSquareIcon className="w-5 h-5" title="Edit" />
                    </button>
                  )}

                  {plan.isActive &&
                    canDelete && (
                      <button
                        onClick={() => openDeleteModal(plan.id)}
                        className="text-red-600"
                      >
                        <TrashIcon className="w-5 h-5" title="Delete" />
                      </button>
                    )}

                  {!plan.isActive &&
                    canRestore && (
                      <button
                        className="text-orange-600"
                        onClick={() => openRestoreModal(plan.id)}
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

            <div className="flex gap-2 mt-4">

              {canView && (
                <button
                  className="flex-1 bg-green-500 text-white py-2 rounded-lg"
                  onClick={() => {
                    setModalMode("view");
                    setSelectedPlan(plan);
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
                    setSelectedPlan(plan);
                    setShowModal(true);
                  }}
                >
                  Edit
                </button>
              )}

              {plan.isActive &&
                canDelete && (
                  <button
                    className="flex-1 bg-red-500 text-white py-2 rounded-lg"
                    onClick={() => openDeleteModal(plan.id)}
                  >
                    Delete
                  </button>
                )}

              {!plan.isActive &&
                canRestore && (
                  <button
                    className="flex-1 bg-orange-500 text-white py-2 rounded-lg"
                    onClick={() => openRestoreModal(plan.id)}
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
            className={`px-4 py-2 rounded ${currentPage === index ? "bg-[#0d4039] text-white" : "bg-gray-200"
              }`}
          >
            {index + 1}
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
