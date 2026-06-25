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
                `/subscription_plans/search?page=${currentPage}&size=10`,
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
                                        onClick={() => {
                                            setModalMode("view");
                                            setSelectedPlan(plan);
                                            setShowModal(true);
                                        }}
                                        className="text-green-600 mr-3"
                                    >
                                        View
                                    </button>

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

                        <div className="flex gap-3 mt-4">


                            <button className="flex-1 bg-green-500 text-white py-2 rounded-xl"
                                onClick={() => {
                                    setModalMode("view");
                                    setSelectedPlan(plan);
                                    setShowModal(true);
                                }}>
                                View
                            </button>

                            <button className="flex-1 bg-blue-500 text-white py-2 rounded-xl"
                                onClick={() => {
                                    setModalMode("edit");
                                    setSelectedPlan(plan);
                                    setShowModal(true);
                                }}>
                                Edit
                            </button>

                            <button className="flex-1 bg-red-500 text-white py-2 rounded-xl">
                                Delete
                            </button>



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
                        className={`px-4 py-2 rounded ${currentPage === index
                            ? "bg-[#0d4039] text-white"
                            : "bg-gray-200"
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