import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function OrganizationModalBox({
  isOpen,
  onClose,
  mode,
  organization,
  onSuccess,
}) {
  const isView = mode === "view";
  const isEdit = mode === "edit";
  const isCreate = mode === "create";

  const fileToDataUrl = (file) =>
    new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = () => resolve(reader.result);
      reader.onerror = () => reject(new Error("Failed to read image"));

      reader.readAsDataURL(file);
    });

  const [subscriptionPlans, setSubscriptionPlans] = useState([]);
  const [saveError, setSaveError] = useState("");

  const [formData, setFormData] = useState({
    organizationName: "",
    logoUrl: "",
    ownerName: "",
    contactNumber: "",
    email: "",
    address: "",
    city: "",
    country: "",
    subscriptionModel: null,
    billingCycle: "MONTHLY",
    subscriptionStartDate: "",
    subscriptionEndDate: "",
    isActive: true,
  });

  const resetForm = () => {
    setFormData({
      organizationName: "",
      logoUrl: "",
      ownerName: "",
      contactNumber: "",
      email: "",
      address: "",
      city: "",
      country: "",
      subscriptionModel: null,
      billingCycle: "MONTHLY",
      subscriptionStartDate: "",
      subscriptionEndDate: "",
      isActive: true,
    });

    setSaveError("");
  };

  const isFormValid =
    formData.organizationName.trim() !== "" &&
    formData.logoUrl !== "" &&
    formData.ownerName.trim() !== "" &&
    formData.contactNumber.trim() !== "" &&
    formData.email.trim() !== "" &&
    formData.address.trim() !== "" &&
    formData.city.trim() !== "" &&
    formData.country.trim() !== "" &&
    formData.subscriptionModel !== null &&
    formData.billingCycle.trim() !== "" &&
    formData.subscriptionStartDate !== "" &&
    formData.subscriptionEndDate !== "";

  useEffect(() => {
    loadSubscriptionPlans();
  }, []);

  useEffect(() => {
    if (organization) {
      setFormData({
        ...organization,
      });
    } else {
      resetForm();
    }
  }, [organization]);

  const loadSubscriptionPlans = async () => {
    try {
      const response = await axiosClient.post(
        "/subscription_plans/search?page=0&size=100",
        {},
      );

      setSubscriptionPlans(response.data.data.content);
    } catch (error) {
      console.error(error);
    }
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
    try {
      await axiosClient.post("/organization", formData);

      onSuccess();
      resetForm();
      onClose();
    } catch (error) {
      console.error(error);
    }
  };

  const handleUpdate = async () => {
    try {
      await axiosClient.put(`/organization/${organization.id}`, formData);

      onSuccess();
      onClose();
    } catch (error) {
      console.error(error);
    }
  };


return (
  <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
    <div className="w-full max-w-6xl max-h-[90vh] overflow-y-auto rounded-2xl bg-white shadow-xl">

      {/* Header */}

      <div className="flex items-center justify-between border-b p-5">
        <h2 className="text-xl font-bold">
          {isCreate && "Create Organization"}
          {isEdit && "Edit Organization"}
          {isView && "Organization Details"}
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

        {/* Organization Name */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Organization Name <span className="text-red-500">*</span>
          </label>

          <input
            name="organizationName"
            value={formData.organizationName}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Organization Name"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
              !formData.organizationName && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Logo */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Logo <span className="text-red-500">*</span>
          </label>

          <input
            type="file"
            accept="image/*"
            disabled={isView}
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
              !formData.logoUrl && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
            onChange={async (e) => {
              setSaveError("");

              const file = e.target.files?.[0];
              if (!file) return;

              if (file.size > 50 * 1024) {
                setSaveError("Maximum file size is 50 KB");
                return;
              }

              try {
                const dataUrl = await fileToDataUrl(file);

                setFormData((prev) => ({
                  ...prev,
                  logoUrl: dataUrl,
                }));
              } catch (err) {
                setSaveError(err?.message || "Failed to read image");
              }
            }}
          />

          {saveError && (
            <p className="mt-2 text-sm text-red-600">
              {saveError}
            </p>
          )}

          {formData.logoUrl && (
            <img
              src={formData.logoUrl}
              alt="Logo Preview"
              className="mt-3 h-24 w-24 rounded-lg border object-cover"
            />
          )}
        </div>

        {/* Owner Name */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Owner Name <span className="text-red-500">*</span>
          </label>

          <input
            name="ownerName"
            value={formData.ownerName}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Owner Name"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.ownerName && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Contact Number */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Contact Number <span className="text-red-500">*</span>
          </label>

          <input
            name="contactNumber"
            value={formData.contactNumber}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Contact Number"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.contactNumber && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Email */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Email <span className="text-red-500">*</span>
          </label>

          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Email"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.email && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Address */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Address <span className="text-red-500">*</span>
          </label>

          <input
            name="address"
            value={formData.address}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Address"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.address && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>


        {/* City */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            City <span className="text-red-500">*</span>
          </label>

          <input
            name="city"
            value={formData.city}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter City"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.city && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Country */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Country <span className="text-red-500">*</span>
          </label>

          <input
            name="country"
            value={formData.country}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Country"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.country && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Subscription Plan */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Subscription Plan <span className="text-red-500">*</span>
          </label>

          <select
            disabled={isView}
            value={formData.subscriptionModel?.id || ""}
            onChange={(e) => {
              const selected = subscriptionPlans.find(
                (p) => p.id === e.target.value,
              );

              setFormData((prev) => ({
                ...prev,
                subscriptionModel: selected || null,
              }));
            }}
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.subscriptionModel && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          >
            <option value="">Select Subscription Plan</option>

            {subscriptionPlans.map((plan) => (
              <option key={plan.id} value={plan.id}>
                {plan.name}
              </option>
            ))}
          </select>
        </div>

        {/* Billing Cycle */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Billing Cycle <span className="text-red-500">*</span>
          </label>

          <select
            name="billingCycle"
            value={formData.billingCycle}
            onChange={handleChange}
            disabled={isView}
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.billingCycle && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          >
            <option value="MONTHLY">MONTHLY</option>
            <option value="QUARTERLY">QUARTERLY</option>
            <option value="YEARLY">YEARLY</option>
          </select>
        </div>

        {/* Subscription Start Date */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Subscription Start Date <span className="text-red-500">*</span>
          </label>

          <input
            type="date"
            name="subscriptionStartDate"
            value={formData.subscriptionStartDate || ""}
            onChange={handleChange}
            disabled={isView}
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.subscriptionStartDate && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Subscription End Date */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Subscription End Date <span className="text-red-500">*</span>
          </label>

          <input
            type="date"
            name="subscriptionEndDate"
            value={formData.subscriptionEndDate || ""}
            onChange={handleChange}
            disabled={isView}
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2  ${
              !formData.subscriptionEndDate && !isView
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Active Organization */}

        <div className="flex items-center md:col-span-2 mt-2">
          <label className="mb-2 block text-sm font-medium">
            <input
              type="checkbox"
              name="isActive"
              checked={formData.isActive}
              onChange={handleChange}
              disabled={isView}
              className="h-4 w-4"
            />
            Active Organization
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