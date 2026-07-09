import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function BranchModalBox({
  isOpen,
  onClose,
  mode,
  branch,
  onSuccess,
}) {
  const isView = mode === "view";
  const isEdit = mode === "edit";
  const isCreate = mode === "create";

  const [organizations, setOrganizations] = useState([]);

  const [formData, setFormData] = useState({
    branchName: "",
    branchCode: "",
    address: "",
    city: "",
    phone: "",
    organizationModel: null,
    isActive: true,
  });

  useEffect(() => {
    if (isOpen) {
      loadOrganizations();
    }
  }, [isOpen]);

  useEffect(() => {
    if (branch) {
      setFormData({
        id: branch.id,
        branchName: branch.branchName || "",
        branchCode: branch.branchCode || "",
        address: branch.address || "",
        city: branch.city || "",
        phone: branch.phone || "",
        organizationModel: branch.organizationModel || null,
        isActive: branch.isActive,
      });
    } else {
      resetForm();
    }
  }, [branch]);

  const resetForm = () => {
    setFormData({
      branchName: "",
      branchCode: "",
      address: "",
      city: "",
      phone: "",
      organizationModel: null,
      isActive: true,
    });
  };

  const loadOrganizations = async () => {
    try {
      const response = await axiosClient.post(
        "/organization/search?page=0&size=100",
        {},
      );

      setOrganizations(response.data.data.content || []);
    } catch (error) {
      console.error("Failed to load organizations", error);
    }
  };

  const handleChange = (e) => {
    const { name, value, checked, type } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleOrganizationChange = (e) => {
    const selected = organizations.find((org) => org.id === e.target.value);

    setFormData((prev) => ({
      ...prev,
      organizationModel: selected || null,
    }));
  };

  const handleSave = async () => {
    try {
      if (isCreate) {
        await axiosClient.post("/branch", formData);
      } else {
        await axiosClient.put(`/branch/${branch.id}`, formData);
      }

      onSuccess();
      onClose();
    } catch (error) {
      console.error(error);

      alert(error?.response?.data?.message || "Unable to save branch.");
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
      <div className="w-full max-w-5xl rounded-2xl bg-white shadow-xl">
        {/* Header */}
        <div className="flex items-center justify-between border-b p-5">
          <h2 className="text-xl font-bold">
            {isCreate && "Create Branch"}
            {isEdit && "Edit Branch"}
            {isView && "Branch Details"}
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
          <div>
            <label className="mb-2 block text-sm font-medium">
              Branch Name
            </label>

            <input
              type="text"
              name="branchName"
              value={formData.branchName}
              onChange={handleChange}
              disabled={isView}
              className="w-full rounded-lg border px-4 py-3"
              placeholder="Enter Branch Name"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium">
              Branch Code
            </label>

            <input
              type="text"
              name="branchCode"
              value={formData.branchCode}
              onChange={handleChange}
              disabled={true}
              className="w-full rounded-lg border px-4 py-3"
              placeholder="Enter Branch Code"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium">Address</label>

            <input
              type="text"
              name="address"
              value={formData.address}
              onChange={handleChange}
              disabled={isView}
              className="w-full rounded-lg border px-4 py-3"
              placeholder="Enter Address"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium">City</label>

            <input
              type="text"
              name="city"
              value={formData.city}
              onChange={handleChange}
              disabled={isView}
              className="w-full rounded-lg border px-4 py-3"
              placeholder="Enter City"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium">Phone</label>

            <input
              type="text"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              disabled={isView}
              className="w-full rounded-lg border px-4 py-3"
              placeholder="Enter Phone"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium">
              Organization
            </label>

            <select
              value={formData.organizationModel?.id || ""}
              onChange={handleOrganizationChange}
              disabled={isView}
              className="w-full rounded-lg border px-4 py-3"
            >
              <option value="">Select Organization</option>

              {organizations.map((org) => (
                <option key={org.id} value={org.id}>
                  {org.organizationName}
                </option>
              ))}
            </select>
          </div>

          <div className="flex items-center">
            <label className="flex items-center gap-3">
              <input
                type="checkbox"
                name="isActive"
                checked={formData.isActive}
                onChange={handleChange}
                disabled={isView}
              />
              Active
            </label>
          </div>
        </div>

        {/* Footer */}
        <div className="flex justify-end gap-3 border-t p-5">
          <button onClick={onClose} className="rounded-lg border px-5 py-2">
            Close
          </button>

          {!isView && (
            <button
              onClick={handleSave}
              className={`rounded-lg px-5 py-2 text-white ${
                isCreate
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
