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

  useEffect(() => {
    loadSubscriptionPlans();
  }, []);

  useEffect(() => {
    if (organization) {
      setFormData({
        ...organization,
      });
    } else {
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
    await axiosClient.post("/organization", formData);

    onSuccess();
    onClose();
  };

  const handleUpdate = async () => {
    await axiosClient.put(`/organization/${organization.id}`, formData);

    onSuccess();
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/40 flex items-center justify-center p-4 overflow-y-auto">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-5xl">
        <div className="border-b p-5 flex justify-between">
          <h2 className="text-xl font-bold">
            {isCreate && "Create Organization"}
            {isEdit && "Edit Organization"}
            {isView && "Organization Details"}
          </h2>

          <button onClick={onClose} className="text-xl">
            ✕
          </button>
        </div>

        <div className="p-6 grid md:grid-cols-2 gap-4">
          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Organization Name
            </label>
            <input
              name="organizationName"
              value={formData.organizationName}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter Organization Name"
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Logo
            </label>

            <input
              type="file"
              accept="image/*"
              disabled={isView}
              className="w-full border rounded-lg px-4 py-3"
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
              <p className="mt-2 text-sm text-red-600">{saveError}</p>
            )}

            {formData.logoUrl && (
              <img
                src={formData.logoUrl}
                alt="Logo Preview"
                className="mt-3 w-24 h-24 object-cover rounded-lg border"
              />
            )}
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Owner Name
            </label>
            <input
              name="ownerName"
              value={formData.ownerName}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter Owner Name"
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Contact Number
            </label>
            <input
              name="contactNumber"
              value={formData.contactNumber}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter Contact Number"
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Email
            </label>
            <input
              name="email"
              value={formData.email}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter Email"
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Address
            </label>
            <input
              name="address"
              value={formData.address}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter Address"
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              City
            </label>
            <input
              name="city"
              value={formData.city}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter City"
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Country
            </label>
            <input
              name="country"
              value={formData.country}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter Country"
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Subscription Plan
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
                  subscriptionModel: selected,
                }));
              }}
              className="w-full border rounded-lg px-4 py-3"
            >
              <option value="">Select Subscription Plan</option>

              {subscriptionPlans.map((plan) => (
                <option key={plan.id} value={plan.id}>
                  {plan.name}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Billing Cycle
            </label>
            <select
              name="billingCycle"
              value={formData.billingCycle}
              onChange={handleChange}
              disabled={isView}
              className="w-full border rounded-lg px-4 py-3"
            >
              <option value="MONTHLY">MONTHLY</option>
              <option value="QUARTERLY">QUARTERLY</option>
              <option value="YEARLY">YEARLY</option>
            </select>
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Subscription Start Date
            </label>
            <input
              type="date"
              name="subscriptionStartDate"
              value={formData.subscriptionStartDate || ""}
              onChange={handleChange}
              disabled={isView}
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Subscription End Date
            </label>
            <input
              type="date"
              name="subscriptionEndDate"
              value={formData.subscriptionEndDate || ""}
              onChange={handleChange}
              disabled={isView}
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          <div className="flex items-center mt-8">
            <label className="flex items-center gap-3 text-sm font-medium text-gray-700">
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

        <div className="border-t p-5 flex justify-end gap-3">
          <button onClick={onClose} className="border px-5 py-2 rounded-lg">
            Close
          </button>

          {isCreate && (
            <button
              onClick={handleSave}
              className="bg-green-600 text-white px-5 py-2 rounded-lg"
            >
              Save
            </button>
          )}

          {isEdit && (
            <button
              onClick={handleUpdate}
              className="bg-blue-600 text-white px-5 py-2 rounded-lg"
            >
              Update
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
