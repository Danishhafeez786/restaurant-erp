import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosClient from "../api/axiosClient";

export default function SubscriptionPlanTable() {

  const navigate = useNavigate();

  const [plans, setPlans] = useState([]);
  const [loading, setLoading] = useState(false);

  const [selectedPlan, setSelectedPlan] = useState(null);

  const [currentPage, setCurrentPage] = useState(0);

  const [totalPages, setTotalPages] = useState(0);

  const [searchCriteria, setSearchCriteria] = useState({
    name: "",
    isActive: "",
    minMonthlyPrice: "",
    maxMonthlyPrice: "",
    minUsersLimit: "",
    maxUsersLimit: ""
  });

  useEffect(() => {
    loadPlans();
  }, [currentPage]);

  const loadPlans = async () => {

    try {

      setLoading(true);

      const payload = {
        name: searchCriteria.name || null,
        isActive:
          searchCriteria.isActive === ""
            ? null
            : searchCriteria.isActive === "true",
        minMonthlyPrice:
          searchCriteria.minMonthlyPrice || null,
        maxMonthlyPrice:
          searchCriteria.maxMonthlyPrice || null,
        minUsersLimit:
          searchCriteria.minUsersLimit || null,
        maxUsersLimit:
          searchCriteria.maxUsersLimit || null
      };

      const response = await axiosClient.post(
        `/subscription_plan/search?page=${currentPage}&size=10`,
        payload
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
    return status
      ? "bg-green-100 text-green-700"
      : "bg-red-100 text-red-700";
  };

  return (
    <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">

      {/* Header */}

      <div className="flex justify-between items-center mb-5">

        <h2 className="text-2xl font-bold">
          Subscription Plans
        </h2>

        <button
          onClick={() => navigate("/create-subscription-plan")}
          className="px-6 py-2 bg-[#0d4039] text-white rounded-lg"
        >
          + Add Plan
        </button>

      </div>

      {/* Filters */}

      <div className="grid md:grid-cols-3 gap-4 mb-5">

        <input
          type="text"
          placeholder="Plan Name"
          value={searchCriteria.name}
          onChange={(e) =>
            setSearchCriteria({
              ...searchCriteria,
              name: e.target.value
            })
          }
          className="border rounded-lg px-4 py-2"
        />

        <select
          value={searchCriteria.isActive}
          onChange={(e) =>
            setSearchCriteria({
              ...searchCriteria,
              isActive: e.target.value
            })
          }
          className="border rounded-lg px-4 py-2"
        >
          <option value="">All Status</option>
          <option value="true">Active</option>
          <option value="false">Inactive</option>
        </select>

        <button
          onClick={() => {
            setCurrentPage(0);
            loadPlans();
          }}
          className="bg-[#0d4039] text-white rounded-lg"
        >
          Search
        </button>

      </div>

      {/* Desktop Table */}

      <div className="hidden md:block overflow-x-auto">

        <table className="w-full">

          <thead>

            <tr className="border-b">

              <th className="py-3 text-left">Name</th>
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

              <tr
                key={plan.id}
                className="border-b hover:bg-gray-50"
              >

                <td className="py-3">
                  {plan.name}
                </td>

                <td>
                  Rs. {plan.monthlyPrice}
                </td>

                <td>
                  Rs. {plan.yearlyPrice}
                </td>

                <td>
                  {plan.branchesLimit}
                </td>

                <td>
                  {plan.usersLimit}
                </td>

                <td>

                  <span
                    className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                      plan.isActive
                    )}`}
                  >
                    {plan.isActive
                      ? "ACTIVE"
                      : "INACTIVE"}
                  </span>

                </td>

                <td>

                  <button
                    onClick={() => setSelectedPlan(plan)}
                    className="text-green-600 mr-3"
                  >
                    View
                  </button>

                  <button
                    className="text-blue-600 mr-3"
                  >
                    Edit
                  </button>

                  <button
                    className="text-red-600"
                  >
                    Delete
                  </button>

                </td>

              </tr>

            ))}

          </tbody>

        </table>

      </div>

      {/* Mobile Cards */}

      <div className="md:hidden space-y-4">

        {plans.map((plan) => (

          <div
            key={plan.id}
            className="border rounded-xl p-4"
          >

            <div className="flex justify-between">

              <h3 className="font-bold">
                {plan.name}
              </h3>

              <span
                className={`px-3 py-1 rounded-full text-xs ${getStatusColor(
                  plan.isActive
                )}`}
              >
                {plan.isActive
                  ? "ACTIVE"
                  : "INACTIVE"}
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

            <button
              onClick={() => setSelectedPlan(plan)}
              className="w-full mt-4 bg-green-600 text-white py-2 rounded-lg"
            >
              View
            </button>

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
              currentPage === index
                ? "bg-[#0d4039] text-white"
                : "bg-gray-200"
            }`}
          >
            {index + 1}
          </button>

        ))}

      </div>

      {/* View Modal */}

      {selectedPlan && (

        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">

          <div className="bg-white rounded-xl w-[95%] md:w-[700px] p-6">

            <div className="flex justify-between mb-4">

              <h2 className="text-xl font-bold">
                Subscription Plan Details
              </h2>

              <button
                onClick={() => setSelectedPlan(null)}
              >
                ✕
              </button>

            </div>

            <div className="grid md:grid-cols-2 gap-4">

              <p><b>Name:</b> {selectedPlan.name}</p>
              <p><b>ID:</b> {selectedPlan.id}</p>

              <p>
                <b>Branches Limit:</b>
                {" "}
                {selectedPlan.branchesLimit}
              </p>

              <p>
                <b>Users Limit:</b>
                {" "}
                {selectedPlan.usersLimit}
              </p>

              <p>
                <b>Menu Items Limit:</b>
                {" "}
                {selectedPlan.menuItemsLimit}
              </p>

              <p>
                <b>Orders Per Month:</b>
                {" "}
                {selectedPlan.ordersPerMonth}
              </p>

              <p>
                <b>Monthly Price:</b>
                Rs. {selectedPlan.monthlyPrice}
              </p>

              <p>
                <b>Yearly Price:</b>
                Rs. {selectedPlan.yearlyPrice}
              </p>

              <p>
                <b>Status:</b>
                {" "}
                {selectedPlan.isActive
                  ? "ACTIVE"
                  : "INACTIVE"}
              </p>

              <p>
                <b>Created At:</b>
                {" "}
                {selectedPlan.createdAt}
              </p>

              <p>
                <b>Updated At:</b>
                {" "}
                {selectedPlan.updatedAt}
              </p>

            </div>

          </div>

        </div>

      )}

    </div>
  );
}