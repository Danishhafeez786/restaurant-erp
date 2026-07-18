import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function PermissionModalBox({
  isOpen,
  onClose,
  mode,
  permission,
  onSuccess,
}) {
  const isView = mode === "view";
  const isEdit = mode === "edit";
  const isCreate = mode === "create";

  const [loading, setLoading] = useState(false);

  const initialFormData = {
    code: "",
    name: "",
    module: "",
    isActive: true,
  };

  const [formData, setFormData] = useState(initialFormData);

  // Required fields validation
  const isFormValid =
    formData.code.trim() !== "" &&
    formData.name.trim() !== "" &&
    formData.module.trim() !== "";

  useEffect(() => {
    if (permission) {
      setFormData({
        id: permission.id,
        code: permission.code || "",
        name: permission.name || "",
        module: permission.module || "",
        isActive: permission.isActive ?? true,
      });
    } else {
      resetForm();
    }
  }, [permission, mode]);

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
    try {
      setLoading(true);

      await axiosClient.post("/permission", formData);

      resetForm(); // prevents duplicate entries

      onSuccess();
      onClose();
    } catch (error) {
      console.error(error);
      alert(error?.response?.data?.message || "Failed to create permission");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdate = async () => {
    try {
      setLoading(true);

      await axiosClient.put(`/permission/${permission.id}`, {
        ...formData,
        id: permission.id,
      });

      resetForm();

      onSuccess();
      onClose();
    } catch (error) {
      console.error(error);
      alert(error?.response?.data?.message || "Failed to update permission");
    } finally {
      setLoading(false);
    }
  };


return (
  <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
    <div className="w-full max-w-6xl max-h-[90vh] overflow-y-auto rounded-2xl bg-white shadow-xl">

      {/* Header */}
      <div className="border-b p-5 flex justify-between">
        <h2 className="text-xl font-bold">
          {isCreate && "Create Permission"}
          {isEdit && "Edit Permission"}
          {isView && "Permission Details"}
        </h2>

        <button
          onClick={onClose}
          className="text-xl"
        >
          ✕
        </button>
      </div>

      {/* Body */}
      <div className="p-6 grid md:grid-cols-2 gap-4">

        {/* Code */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Code <span className="text-red-500">*</span>
          </label>

          <input
            name="code"
            value={formData.code}
            onChange={handleChange}
            disabled={!isCreate}
            placeholder="Enter Code"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
              !isCreate ? "bg-gray-100" : ""
            } border ${
              !isView && formData.code.trim() === ""
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Name */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Name <span className="text-red-500">*</span>
          </label>

          <input
            name="name"
            value={formData.name}
            onChange={handleChange}
            disabled={!isCreate}
            placeholder="Enter Name"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
              !isCreate ? "bg-gray-100" : ""
            } border ${
              !isView && formData.name.trim() === ""
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Module */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Module <span className="text-red-500">*</span>
          </label>

          <input
            name="module"
            value={formData.module}
            onChange={handleChange}
            disabled={isView}
            placeholder="e.g. User Management"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
              !isView && formData.module.trim() === ""
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>


        {/* Active Permission */}

        {!isCreate && (
          <div className="flex items-center mt-8">
            <label className="mb-2 block text-sm font-medium">
              <input
                type="checkbox"
                name="isActive"
                checked={formData.isActive}
                onChange={handleChange}
                disabled={isView}
                className="h-4 w-4"
              />
              Active Permission
            </label>
          </div>
        )}

      </div>

      {/* Footer */}

      <div className="border-t p-5 flex justify-end gap-3">

        <button
          onClick={onClose}
          className="border px-5 py-2 rounded-lg hover:bg-gray-100 transition"
        >
          Close
        </button>

        {isCreate && (
          <button
            onClick={handleSave}
            disabled={loading || !isFormValid}
            className={`px-5 py-2 rounded-lg text-white transition ${
              loading || !isFormValid
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-green-600 hover:bg-green-700"
            }`}
          >
            {loading ? "Creating..." : "Save"}
          </button>
        )}

        {isEdit && (
          <button
            onClick={handleUpdate}
            disabled={loading || !isFormValid}
            className={`px-5 py-2 rounded-lg text-white transition ${
              loading || !isFormValid
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-blue-600 hover:bg-blue-700"
            }`}
          >
            {loading ? "Updating..." : "Update"}
          </button>
        )}

      </div>

    </div>
  </div>
);
}