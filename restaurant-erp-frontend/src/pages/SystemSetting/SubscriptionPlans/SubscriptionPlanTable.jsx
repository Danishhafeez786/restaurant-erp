import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosClient from "../../../api/axiosClient";
import SubscriptionPlanModal from "./SubscriptionPlanModalBox";
import FilterField from "../../../components/FilterField";
import CustomSelect from "../../../components/CustomSelect";
import { Listbox } from "@headlessui/react";
import {
  PlusIcon,
  EyeIcon,
  PencilSquareIcon,
  TrashIcon,
  ArrowPathIcon,
  ChevronUpIcon,
  ChevronDownIcon,
  ChevronUpDownIcon,
  CheckIcon,
  MagnifyingGlassIcon,
  FunnelIcon,
} from "@heroicons/react/24/outline";
import { toast } from "react-toastify";
import { Trash, ScanEye, PenLine, Users, Building2 } from "lucide-react";
const statusOptions = [
  { label: "All", value: "" },
  { label: "Active", value: "true" },
  { label: "Inactive", value: "false" },
];

const orderOptions = [
  { label: "Descending", value: "DESC" },
  { label: "Ascending", value: "ASC" },
];

const pageSizeOptions = [
  { label: "10", value: 10 },
  { label: "25", value: 25 },
  { label: "50", value: 50 },
  { label: "100", value: 100 },
];
const API_URL = import.meta.env.VITE_API_URL;

export default function SubscriptionPlanTable() {
  const navigate = useNavigate();

  const [plans, setPlans] = useState([]);

  const [loading, setLoading] = useState(false);
  const [showFilters, setShowFilters] = useState(false);

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
  });

  const statuses = [
    { name: "All", value: "" },
    { name: "Active", value: "true" },
    { name: "Inactive", value: "false" },
  ];

  const selectedStatus =
    statuses.find((status) => status.value === searchCriteria.isActive) ||
    statuses[0];

  const handleSort = (field) => {
    setCurrentPage(0);

    if (sortBy === field) {
      setDirection((prev) => (prev === "ASC" ? "DESC" : "ASC"));
    } else {
      setSortBy(field);
      setDirection("ASC");
    }
  };

  const SortableHeader = ({ label, field }) => {
    const active = sortBy === field;

    return (
      <th
        onClick={() => handleSort(field)}
        className="cursor-pointer px-4 py-3 text-left hover:bg-gray-200"
      >
        <div className="flex items-center gap-1">
          <span>{label}</span>

          {active ? (
            direction === "ASC" ? (
              <ChevronUpIcon className="h-4 w-4 text-[#0d4039]" />
            ) : (
              <ChevronDownIcon className="h-4 w-4 text-[#0d4039]" />
            )
          ) : (
            <div className="flex flex-col leading-none opacity-40">
              <ChevronUpIcon className="h-3 w-3 -mb-1" />
              <ChevronDownIcon className="h-3 w-3" />
            </div>
          )}
        </div>
      </th>
    );
  };

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
            error?.response?.data?.message || "Failed to delete plan",
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
            error?.response?.data?.message || "Failed to restore plan",
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
    const eventSource = new EventSource(`${API_URL}/subscription_plans/stream`);

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
            : searchCriteria.isActive === "true",
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
    return status ? "bg-green-200 text-green-700" : "bg-red-100 text-red-700";
  };

  return (
    <div>
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        {/* =========================== HEADER =========================== */}
        <div className="flex justify-between items-center mb-5">
          <h2 className="text-2xl font-bold">Subscription Plans</h2>

          {canCreate && (
            <button
              onClick={() => {
                setModalMode("create");
                setSelectedPlan(null);
                setShowModal(true);
              }}
              className="px-3 sm:px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium flex items-center justify-center gap-2"
            >
              <PlusIcon className="w-5 h-5" />
              <span className="hidden sm:inline">Add Plan</span>
            </button>
          )}
        </div>

        <div>
          <div className="lg:hidden space-y-4">
            <div className="lg:hidden">
              <div className="relative">
                <input
                  type="text"
                  value={searchCriteria.name}
                  onChange={(e) =>
                    setSearchCriteria((prev) => ({
                      ...prev,
                      name: e.target.value,
                    }))
                  }
                  placeholder="Search Plan..."
                  className="w-full rounded-xl border border-gray-300 bg-white py-3 pl-11 pr-12 text-sm shadow-sm outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                />

                {/* Search Icon */}
                <MagnifyingGlassIcon className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />

                {/* Filter Icon */}
                <button
                  onClick={() => setShowFilters(!showFilters)}
                  className="absolute right-2 top-1/2 -translate-y-1/2 rounded-lg p-2 hover:bg-gray-100"
                >
                  <FunnelIcon
                    className={`h-5 w-5 transition-all duration-300 ${
                      showFilters ? "rotate-180 text-blue-600" : "text-gray-500"
                    }`}
                  />
                </button>
              </div>
            </div>
          </div>

          {/* Mobile Filter Toggle */}
          <div
            className={`overflow-hidden transition-all duration-300 ease-in-out ${
              showFilters
                ? "max-h-[500px] opacity-100 mt-4"
                : "max-h-0 opacity-0"
            }`}
          >
            <div className=" shadow-sm space-y-4">
              {/* Hidden on mobile until expanded */}
              <div
                className={`
                space-y-4
                ${showFilters ? "block" : "hidden"}
                lg:block
              `}
              >
                <FilterField label="Status">
                  <CustomSelect
                    options={statusOptions}
                    value={searchCriteria.isActive}
                    onChange={(item) =>
                      setSearchCriteria((prev) => ({
                        ...prev,
                        isActive: item.value,
                      }))
                    }
                  />
                </FilterField>

                <FilterField label="Order">
                  <CustomSelect
                    options={orderOptions}
                    value={direction}
                    onChange={(item) => {
                      setCurrentPage(0);
                      setDirection(item.value);
                    }}
                  />
                </FilterField>

                <FilterField label="Rows">
                  <CustomSelect
                    options={pageSizeOptions}
                    value={pageSize}
                    onChange={(item) => {
                      setCurrentPage(0);
                      setPageSize(item.value);
                    }}
                  />
                </FilterField>

                <button
                  onClick={() => {
                    setSearchCriteria({
                      name: "",
                      isActive: "",
                    });

                    setSortBy("createdAt");
                    setDirection("DESC");
                    setPageSize(10);
                    setCurrentPage(0);
                  }}
                  className="w-full h-12 rounded-xl bg-gray-100 font-medium hover:bg-gray-200 flex items-center justify-center gap-2"
                >
                  <ArrowPathIcon className="h-5 w-5" />
                  Reset
                </button>
              </div>
            </div>
          </div>
          {/* ================= Desktop ================= */}
          <div className="hidden lg:flex flex-wrap items-center gap-4">
            {/* Plan Name */}

            <FilterField label="Search" className="flex-1 min-w-[300px]">
              <input
                type="text"
                value={searchCriteria.name}
                onChange={(e) =>
                  setSearchCriteria((prev) => ({
                    ...prev,
                    name: e.target.value,
                  }))
                }
                placeholder="Plan Name...."
                className="w-full border-0 bg-transparent text-sm focus:outline-none"
              />
            </FilterField>

            {/* Status */}

            <FilterField
              label="Status"
              className="w-full xl:flex-1 xl:min-w-[300px]"
            >
              <CustomSelect
                options={statusOptions}
                value={searchCriteria.isActive}
                onChange={(item) =>
                  setSearchCriteria((prev) => ({
                    ...prev,
                    isActive: item.value,
                  }))
                }
              />
            </FilterField>

            {/* Order */}

            <FilterField
              label="Order"
              className="w-full xl:flex-1 xl:min-w-[300px]"
            >
              <CustomSelect
                options={orderOptions}
                value={direction}
                onChange={(item) => {
                  setCurrentPage(0);
                  setDirection(item.value);
                }}
              />
            </FilterField>

            {/* Page Size */}

            <FilterField
              label="Rows"
              className="w-full xl:flex-1 xl:min-w-[50px]"
            >
              <CustomSelect
                options={pageSizeOptions}
                value={pageSize}
                onChange={(item) => {
                  setCurrentPage(0);
                  setPageSize(item.value);
                }}
              />
            </FilterField>

            {/* Reset */}

            <button
              onClick={() => {
                setSearchCriteria({
                  name: "",
                  isActive: "",
                });

                setSortBy("createdAt");
                setDirection("DESC");
                setPageSize(10);
                setCurrentPage(0);
              }}
              className="w-full md:w-auto h-12 flex items-center justify-center gap-2 rounded-xl bg-gray-100 px-6 font-bold transition hover:bg-gray-200 on-slect-hover:outline-black focus:outline-none focus:ring-2 focus:ring-gray-300"
            >
              <ArrowPathIcon className="h-5 w-5" />
              <span>Reset</span>
            </button>
          </div>
        </div>
      </div>

      {/* Desktop Table */}
      <br />
      <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">
        <div className=" flex items-center justify-between mb-4 ">
          <p className="text-sm text-gray-500">
            Showing{" "}
            <span className="font-semibold text-blue-500">{plans.length}</span>{" "}
            Subscription Plans
          </p>
        </div>
        <div className="hidden lg:block overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
          <table className="min-w-full">
            <thead className="border-b border-gray-200 bg-gray-50">
              <tr className="bg-gray-100">
                <SortableHeader label="Name" field="name" />
                <SortableHeader label="Monthly" field="monthlyPrice" />

                <SortableHeader label="Yearly" field="yearlyPrice" />

                <SortableHeader label="Branches" field="branchesLimit" />

                <SortableHeader label="Users" field="usersLimit" />

                <SortableHeader label="Status" field="isActive" />
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
                        className="text-blue-500 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1"
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
                        className="text-blue-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                      >
                        <PenLine className="w-5 h-5" title="Edit" />
                      </button>
                    )}

                    {plan.isActive && canDelete && (
                      <button
                        onClick={() => openDeleteModal(plan.id)}
                        className="text-red-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
                      >
                        <Trash className="w-5 h-5" title="Delete" />
                      </button>
                    )}

                    {!plan.isActive && canRestore && (
                      <button
                        className="text-orange-600 mr-3 items-center text-sm hover:bg-gray-50 border rounded-lg p-1 "
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

        {/* Mobile List */}
        <div className="lg:hidden rounded-2xl border border-gray-200 bg-white overflow-hidden shadow-sm">
          {plans.map((plan) => (
            <div
              key={plan.id}
              className="
        px-4
        py-3
        border-b
        last:border-b-0
        hover:bg-blue-50/40
        transition
      "
            >
              {/* Row 1 */}
              <div className="flex items-start justify-between gap-3">
                <div className="min-w-0 flex-1">
                  <div className="flex items-center gap-2">
                    <h3 className="truncate font-semibold text-gray-900">
                      {plan.name}
                    </h3>

                    <span
                      className={`rounded-full px-2 py-0.5 text-[10px] font-semibold ${
                        plan.isActive
                          ? "bg-green-100 text-green-700"
                          : "bg-red-100 text-red-700"
                      }`}
                    >
                      {plan.isActive ? "ACTIVE" : "INACTIVE"}
                    </span>
                  </div>

                  <p className="mt-1 text-xs text-gray-500">
                    Rs. {plan.monthlyPrice}/month • Rs. {plan.yearlyPrice}/year
                  </p>
                </div>
              </div>

              {/* Row 2 */}

              <div className="mt-3 flex items-center justify-between">
                <div className="flex items-center gap-4 text-xs text-gray-500">
                  <div className="flex items-center gap-1">
                    <Users className="h-3.5 w-3.5 text-blue-500" />
                    <span>{plan.usersLimit}</span>
                  </div>

                  <div className="flex items-center gap-1">
                    <Building2 className="h-3.5 w-3.5 text-blue-500" />
                    <span>{plan.branchesLimit}</span>
                  </div>
                </div>

                <div className="flex items-center">
                  {canView && (
                    <button
                      onClick={() => {
                        setModalMode("view");
                        setSelectedPlan(plan);
                        setShowModal(true);
                      }}
                      className="rounded-lg p-2 text-blue-600 hover:bg-blue-100"
                    >
                      <EyeIcon className="h-5 w-5" />
                    </button>
                  )}

                  {canUpdate && (
                    <button
                      onClick={() => {
                        setModalMode("edit");
                        setSelectedPlan(plan);
                        setShowModal(true);
                      }}
                      className="rounded-lg p-2 text-indigo-600 hover:bg-indigo-100"
                    >
                      <PenLine className="h-5 w-5" />
                    </button>
                  )}

                  {plan.isActive && canDelete && (
                    <button
                      onClick={() => openDeleteModal(plan.id)}
                      className="rounded-lg p-2 text-red-600 hover:bg-red-100"
                    >
                      <Trash className="h-5 w-5" />
                    </button>
                  )}

                  {!plan.isActive && canRestore && (
                    <button
                      onClick={() => openRestoreModal(plan.id)}
                      className="rounded-lg p-2 text-orange-600 hover:bg-orange-100"
                    >
                      <ArrowPathIcon className="h-5 w-5" />
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* ================= Pagination ================= */}

        <div className="mt-6 flex  flex-col gap-4 border-t border-gray-200 pt-5 md:flex-row md:items-center md:justify-between">
          {/* Left */}

          <div className="text-sm text-gray-500 ">
            Showing{" "}
            <span className="font-semibold text-blue-500">
              {plans.length === 0 ? 0 : currentPage * pageSize + 1}
            </span>{" "}
            to{" "}
            <span className="font-semibold text-blue-500 ">
              {Math.min(
                (currentPage + 1) * pageSize,
                currentPage * pageSize + plans.length,
              )}
            </span>{" "}
            results
          </div>

          {/* Right */}

          <div className="flex items-center gap-2 ">
            {/* Previous */}

            <button
              disabled={currentPage === 0}
              onClick={() => setCurrentPage((prev) => prev - 1)}
              className={`rounded-xl border px-4 py-2 text-sm transition 
      ${
        currentPage === 0
          ? "cursor-not-allowed border-gray-200 text-gray-400 "
          : "border-gray-300 hover:bg-gray-50"
      }`}
            >
              Previous
            </button>

            {/* Pages */}

            {[...Array(totalPages)].map((_, index) => (
              <button
                key={index}
                onClick={() => setCurrentPage(index)}
                className={`h-10 w-10 rounded-xl text-sm font-medium transition bg-blue-600 hover:bg-blue-700

        ${
          currentPage === index
            ? "bg-[#0d4039] text-white shadow-sm"
            : "border border-gray-200 bg-white hover:bg-gray-50"
        }`}
              >
                {index + 1}
              </button>
            ))}

            {/* Next */}

            <button
              disabled={currentPage === totalPages - 1 || totalPages === 0}
              onClick={() => setCurrentPage((prev) => prev + 1)}
              className={`rounded-xl border px-4 py-2 text-sm transition

      ${
        currentPage === totalPages - 1 || totalPages === 0
          ? "cursor-not-allowed border-gray-200 text-gray-400"
          : "border-gray-300 hover:bg-gray-50"
      }`}
            >
              Next
            </button>
          </div>
        </div>

        {confirmModal.open && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div className="bg-white rounded-xl shadow-lg w-[90%] max-w-md p-6">
              <h2 className="text-xl font-bold text-gray-800">
                {confirmModal.title}
              </h2>

              <p className="mt-3 text-gray-600">{confirmModal.message}</p>

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
    </div>
  );
}
