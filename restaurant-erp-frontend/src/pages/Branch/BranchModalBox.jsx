import { useEffect, useMemo, useState } from "react";
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
        address: branch.address || "",
        city: branch.city || "",
        phone: branch.phone || "",
        organizationModel: branch.organizationModel || null,
        isActive:
          branch.isActive !== undefined ? branch.isActive : true,
      });
    } else {
      resetForm();
    }
  }, [branch]);

  const resetForm = () => {
    setFormData({
      branchName: "",
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
        {}
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
    const selected = organizations.find(
      (org) => String(org.id) === String(e.target.value)
    );

    setFormData((prev) => ({
      ...prev,
      organizationModel: selected || null,
    }));
  };

  const isFormValid = useMemo(() => {
    return (
      formData.branchName.trim() !== "" &&
      formData.address.trim() !== "" &&
      formData.city.trim() !== "" &&
      formData.phone.trim() !== "" &&
      formData.organizationModel !== null
    );
  }, [formData]);

  const handleSave = async () => {
  if (!isFormValid) {
    return;
  }

  try {
    if (isCreate) {
      await axiosClient.post("/branch", formData);

      await onSuccess();

      resetForm();
    } else {
      await axiosClient.put(`/branch/${branch.id}`, formData);

      await onSuccess();

      onClose();
    }
  } catch (error) {
    console.error(error);

    alert(error?.response?.data?.message || "Unable to save branch.");
  }
};

    if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
      <div className="w-full max-w-6xl max-h-[90vh] overflow-y-auto rounded-2xl bg-white shadow-xl">
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

          {/* Branch Name */}
          <div>
            <label className="mb-2 block text-sm font-medium">
              Branch Name <span className="text-red-500">*</span>
            </label>

            <input
              type="text"
              name="branchName"
              value={formData.branchName}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter Branch Name"
              className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                !isView && formData.branchName.trim() === ""
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
              type="text"
              name="address"
              value={formData.address}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter Address"
              className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                !isView && formData.address.trim() === ""
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
              type="text"
              name="city"
              value={formData.city}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter City"
              className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                !isView && formData.city.trim() === ""
                  ? "border-gray-300 focus:ring-red-400"
                  : "border-gray-300 focus:ring-blue-500"
              }`}
            />
          </div>


          {/* Phone */}
          <div>
            <label className="mb-2 block text-sm font-medium">
              Phone <span className="text-red-500">*</span>
            </label>

            <input
              type="text"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              disabled={isView}
              placeholder="Enter Phone"
              className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                !isView && formData.phone.trim() === ""
                  ? "border-gray-300 focus:ring-red-400"
                  : "border-gray-300 focus:ring-blue-500"
              }`}
            />
          </div>

          {/* Organization */}
          <div>
            <label className="mb-2 block text-sm font-medium">
              Organization <span className="text-red-500">*</span>
            </label>

            <select
              value={formData.organizationModel?.id || ""}
              onChange={handleOrganizationChange}
              disabled={isView}
              className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                !isView && !formData.organizationModel
                  ? "border-gray-300 focus:ring-red-400"
                  : "border-gray-300 focus:ring-blue-500"
              }`}
            >
              <option value="">Select Organization</option>

              {organizations.map((org) => (
                <option key={org.id} value={org.id}>
                  {org.organizationName}
                </option>
              ))}
            </select>
          </div>

          {/* Active */}
          <div className="flex items-center md:col-span-2">
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
          <button
            onClick={onClose}
            className="rounded-lg border px-5 py-2 hover:bg-gray-100"
          >
            Close
          </button>

          {!isView && (
            <button
              onClick={handleSave}
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