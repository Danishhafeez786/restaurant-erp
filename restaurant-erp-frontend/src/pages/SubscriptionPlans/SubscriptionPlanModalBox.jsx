import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

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

  const [formData, setFormData] = useState({
    name: "",
    branchesLimit: "",
    usersLimit: "",
    menuItemsLimit: "",
    ordersPerMonth: "",
    monthlyPrice: "",
    yearlyPrice: "",
    isActive: true,
  });

  useEffect(() => {
    if (plan) {
      setFormData({
        name: plan.name || "",
        branchesLimit: plan.branchesLimit || "",
        usersLimit: plan.usersLimit || "",
        menuItemsLimit: plan.menuItemsLimit || "",
        ordersPerMonth: plan.ordersPerMonth || "",
        monthlyPrice: plan.monthlyPrice || "",
        yearlyPrice: plan.yearlyPrice || "",
        isActive: plan.isActive,
      });
    } else {
      setFormData({
        name: "",
        branchesLimit: "",
        usersLimit: "",
        menuItemsLimit: "",
        ordersPerMonth: "",
        monthlyPrice: "",
        yearlyPrice: "",
        isActive: true,
      });
    }
  }, [plan]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSave = async () => {
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

      onSuccess();
      onClose();
    } catch (error) {
      console.error(error);
    }
  };

  const handleUpdate = async () => {
    try {
      await axiosClient.put(`/subscription_plans/${plan.id}`, {
        ...formData,
        id: plan.id,
      });

      onSuccess();
      onClose();
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/40 flex items-center justify-center p-4 overflow-y-auto">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-4xl">
        <div className="flex justify-between items-center border-b p-5">
          <h2 className="text-xl font-bold">
            {isCreate && "Create Subscription Plan"}
            {isEdit && "Edit Subscription Plan"}
            {isView && "Subscription Plan Details"}
          </h2>

          <button onClick={onClose} className="text-xl">
            ✕
          </button>
        </div>

        <div className="p-6 grid md:grid-cols-2 gap-4">
          <input
            name="name"
            value={formData.name}
            onChange={handleChange}
            disabled={isView}
            placeholder="Plan Name"
            className="border rounded-lg px-4 py-3"
          />

          <input
            name="branchesLimit"
            value={formData.branchesLimit}
            onChange={handleChange}
            disabled={isView}
            placeholder="Branches Limit"
            className="border rounded-lg px-4 py-3"
          />

          <input
            name="usersLimit"
            value={formData.usersLimit}
            onChange={handleChange}
            disabled={isView}
            placeholder="Users Limit"
            className="border rounded-lg px-4 py-3"
          />

          <input
            name="menuItemsLimit"
            value={formData.menuItemsLimit}
            onChange={handleChange}
            disabled={isView}
            placeholder="Menu Items Limit"
            className="border rounded-lg px-4 py-3"
          />

          <input
            name="ordersPerMonth"
            value={formData.ordersPerMonth}
            onChange={handleChange}
            disabled={isView}
            placeholder="Orders Per Month"
            className="border rounded-lg px-4 py-3"
          />

          <input
            name="monthlyPrice"
            value={formData.monthlyPrice}
            onChange={handleChange}
            disabled={isView}
            placeholder="Monthly Price"
            className="border rounded-lg px-4 py-3"
          />

          <input
            name="yearlyPrice"
            value={formData.yearlyPrice}
            onChange={handleChange}
            disabled={isView}
            placeholder="Yearly Price"
            className="border rounded-lg px-4 py-3"
          />

          <label className="flex items-center gap-3">
            <input
              type="checkbox"
              name="isActive"
              checked={formData.isActive}
              onChange={handleChange}
              disabled={isView}
            />
            Active Plan
          </label>
        </div>

        <div className="flex justify-end gap-3 border-t p-5">
          <button onClick={onClose} className="px-5 py-2 border rounded-lg">
            Close
          </button>

          {isCreate && (
            <button
              onClick={handleSave}
              className="px-5 py-2 bg-green-600 text-white rounded-lg"
            >
              Save
            </button>
          )}

          {isEdit && (
            <button
              onClick={handleUpdate}
              className="px-5 py-2 bg-blue-600 text-white rounded-lg"
            >
              Update
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
