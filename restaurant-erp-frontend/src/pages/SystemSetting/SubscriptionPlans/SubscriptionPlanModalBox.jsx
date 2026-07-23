import { useEffect, useState } from "react";
import axiosClient from "../../../api/axiosClient";
import { toast } from "react-toastify";

export default function SubscriptionPlanModalBox({
  isOpen,
  onClose,
  mode,
  plan,
  onSuccess,
}) {
  const isView = mode === "view";
  const isEdit = mode === "edit";
  const isCreate = mode === "create";

  const initialFormData = {
    name: "",
    branchesLimit: "",
    usersLimit: "",
    menuItemsLimit: "",
    ordersPerMonth: "",
    monthlyPrice: "",
    yearlyPrice: "",
    isActive: true,
  };

  const [formData, setFormData] = useState(initialFormData);

  const isFormValid =
    formData.name.trim() !== "" &&
    formData.branchesLimit !== "" &&
    formData.usersLimit !== "" &&
    formData.menuItemsLimit !== "" &&
    formData.ordersPerMonth !== "" &&
    formData.monthlyPrice !== "" &&
    formData.yearlyPrice !== "";

  useEffect(() => {
    if (!isOpen) return;

    if (plan) {
      setFormData({
        name: plan.name || "",
        branchesLimit: plan.branchesLimit || "",
        usersLimit: plan.usersLimit || "",
        menuItemsLimit: plan.menuItemsLimit || "",
        ordersPerMonth: plan.ordersPerMonth || "",
        monthlyPrice: plan.monthlyPrice || "",
        yearlyPrice: plan.yearlyPrice || "",
        isActive:
          plan.isActive !== undefined ? plan.isActive : true,
      });
    } else {
      resetForm();
    }
  }, [plan, isOpen]);

  const resetForm = () => {
    setFormData(initialFormData);
  };

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSave = async () => {
    if (!isFormValid) return;

    try {
      await axiosClient.post("/subscription_plans", {
        ...formData,
        branchesLimit: Number(formData.branchesLimit),
        usersLimit: Number(formData.usersLimit),
        menuItemsLimit: Number(formData.menuItemsLimit),
        ordersPerMonth: Number(formData.ordersPerMonth),
        monthlyPrice: Number(formData.monthlyPrice),
        yearlyPrice: Number(formData.yearlyPrice),
      });

      resetForm();
      onSuccess();
      onClose();
    } catch (error) {
      toast.error(error?.response?.data?.message || "Unable to save subscription plan.");
    }
  };

  const handleUpdate = async () => {
    if (!isFormValid) return;

    try {
      await axiosClient.put(`/subscription_plans/${plan.id}`, {
        ...formData,
        id: plan.id,
        branchesLimit: Number(formData.branchesLimit),
        usersLimit: Number(formData.usersLimit),
        menuItemsLimit: Number(formData.menuItemsLimit),
        ordersPerMonth: Number(formData.ordersPerMonth),
        monthlyPrice: Number(formData.monthlyPrice),
        yearlyPrice: Number(formData.yearlyPrice),
      });

      onSuccess();
      onClose();
    } catch (error) {
      console.error(error);

      toast.error(error?.response?.data?.message || "Unable to update subscription plan.");  
    }
  };


return (
  <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
    <div className="w-full max-w-6xl max-h-[90vh] overflow-y-auto rounded-2xl bg-white shadow-xl">

      {/* Header */}

      <div className="flex items-center justify-between border-b p-5">
        <h2 className="text-xl font-bold">
          {isCreate && "Create Subscription Plan"}
          {isEdit && "Edit Subscription Plan"}
          {isView && "Subscription Plan Details"}
        </h2>

        <button
          onClick={onClose}
          className="text-2xl text-gray-500 hover:text-black"
        >
          ×
        </button>
      </div>

      {/* Body */}

      <div className="grid gap-5 p-6 md:grid-cols-2">

        {/* Plan Name */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Plan Name <span className="text-red-500">*</span>
          </label>

          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Plan Name"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.name && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Branches Limit */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Branches Limit <span className="text-red-500">*</span>
          </label>

          <input
            type="number"
            name="branchesLimit"
            value={formData.branchesLimit}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Branches Limit"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.branchesLimit && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Users Limit */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Users Limit <span className="text-red-500">*</span>
          </label>

          <input
            type="number"
            name="usersLimit"
            value={formData.usersLimit}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Users Limit"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.usersLimit && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Menu Items Limit */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Menu Items Limit <span className="text-red-500">*</span>
          </label>

          <input
            type="number"
            name="menuItemsLimit"
            value={formData.menuItemsLimit}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Menu Items Limit"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.menuItemsLimit && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Orders Per Month */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Orders Per Month <span className="text-red-500">*</span>
          </label>

          <input
            type="number"
            name="ordersPerMonth"
            value={formData.ordersPerMonth}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Orders Per Month"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.ordersPerMonth && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>


        {/* Monthly Price */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Monthly Rent <span className="text-red-500">*</span>
          </label>

          <input
            type="number"
            name="monthlyPrice"
            value={formData.monthlyPrice}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Monthly Price"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.monthlyPrice && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Yearly Price */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Yearly Rent (If One installment) <span className="text-red-500">*</span>
          </label>

          <input
            type="number"
            name="yearlyPrice"
            value={formData.yearlyPrice}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Yearly Price"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.yearlyPrice && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Active */}

        <div className="flex items-center md:col-span-2">
          <label className="mb-2 block text-sm font-medium">
            <input
              type="checkbox"
              name="isActive"
              checked={formData.isActive}
              onChange={handleChange}
              disabled={isView}
              className="h-4 w-4"
            />
            Active Plan
          </label>
        </div>

      </div>

      {/* Footer */}

      <div className="flex justify-end gap-3 border-t p-5">

        <button
          onClick={onClose}
          className="rounded-lg border px-5 py-2"
        >
          Close
        </button>

        {!isView && (
          <button
            onClick={isCreate ? handleSave : handleUpdate}
            disabled={!isFormValid}
            className={`rounded-lg px-5 py-2 text-white transition ${
              !isFormValid
                ? "cursor-not-allowed bg-gray-400"
                : isCreate
                ? "bg-green-600 hover:bg-green-700"
                : "bg-blue-600 hover:bg-blue-700"
            }`}
          >
            {isCreate ? "Save" : "Update"}
          </button>
        )}

      </div>

    </div>
  </div>
);
}