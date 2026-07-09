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

  const [formData, setFormData] = useState({
    code: "",
    name: "",
    module: "",
    isActive: true,
  });

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
      setFormData({
        code: "",
        name: "",
        module: "",
        isActive: true,
      });
    }
  }, [permission, mode]);

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

      await axiosClient.put(`/permission/${permission.id}`, formData);

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
    <div className="fixed inset-0 z-50 bg-black/40 flex items-center justify-center p-4 overflow-y-auto">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-3xl">

        {/* Header */}
        <div className="border-b p-5 flex justify-between">
          <h2 className="text-xl font-bold">
            {isCreate && "Create Permission"}
            {isEdit && "Edit Permission"}
            {isView && "Permission Details"}
          </h2>

          <button onClick={onClose} className="text-xl">
            ✕
          </button>
        </div>

        {/* Body */}
        <div className="p-6 grid md:grid-cols-2 gap-4">

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Code
            </label>

            <input
              name="code"
              value={formData.code}
              onChange={handleChange}
              disabled={!isCreate}
              placeholder="Enter Code"
              className="w-full border rounded-lg px-4 py-3 bg-gray-100"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Name
            </label>

            <input
              name="name"
              value={formData.name}
              onChange={handleChange}
              disabled={!isCreate}
              placeholder="Enter Name"
              className="w-full border rounded-lg px-4 py-3 bg-gray-100"
            />
          </div>

          <div>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Module
            </label>

            <input
              name="module"
              value={formData.module}
              onChange={handleChange}
              disabled={isView}
              placeholder="e.g. User Management"
              className="w-full border rounded-lg px-4 py-3"
            />
          </div>

          {!isCreate && (
            <div className="flex items-center mt-8">
              <label className="flex items-center gap-3 text-sm font-medium text-gray-700">
                <input
                  type="checkbox"
                  name="isActive"
                  checked={formData.isActive}
                  onChange={handleChange}
                  disabled={isView}
                />
                Active Permission
              </label>
            </div>
          )}

        </div>

        {/* Footer */}
        <div className="border-t p-5 flex justify-end gap-3">

          <button onClick={onClose} className="border px-5 py-2 rounded-lg">
            Close
          </button>

          {isCreate && (
            <button
              onClick={handleSave}
              disabled={loading}
              className="bg-green-600 text-white px-5 py-2 rounded-lg"
            >
              {loading ? "Creating..." : "Generate Permissions"}
            </button>
          )}

          {isEdit && (
            <button
              onClick={handleUpdate}
              disabled={loading}
              className="bg-blue-600 text-white px-5 py-2 rounded-lg"
            >
              {loading ? "Updating..." : "Update"}
            </button>
          )}

        </div>

      </div>
    </div>
  );
}