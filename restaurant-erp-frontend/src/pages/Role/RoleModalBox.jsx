import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function RoleModalBox({
  isOpen,
  onClose,
  mode,
  role,
  onSuccess,
}) {
  const isView = mode === "view";
  const isEdit = mode === "edit";
  const isCreate = mode === "create";

  const [organizations, setOrganizations] = useState([]);

  const initialFormData = {
    roleName: "",
    description: "",
    organizationModel: null,
    isActive: true,
  };

  const [formData, setFormData] = useState(initialFormData);

  // Required fields validation
  const isFormValid =
    formData.roleName.trim() !== "" &&
    formData.description.trim() !== "" &&
    formData.organizationModel !== null;

  useEffect(() => {
    loadOrganizations();
  }, []);

  useEffect(() => {
    if (role) {
      setFormData({
        roleName: role.roleName || "",
        description: role.description || "",
        organizationModel: role.organizationModel || null,
        isActive: role.isActive ?? true,
      });
    } else {
      resetForm();
    }
  }, [role]);

  const resetForm = () => {
    setFormData(initialFormData);
  };

  const loadOrganizations = async () => {
    try {
      const response = await axiosClient.post(
        "/organization/search?page=0&size=100",
        {}
      );

      setOrganizations(response.data.data.content);
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
      await axiosClient.post("/role", formData);

      resetForm(); // prevents duplicate entries

      onSuccess();
      onClose();
    } catch (error) {
      console.error(error);
    }
  };

  const handleUpdate = async () => {
    try {
      await axiosClient.put(`/role/${role.id}`, {
        ...formData,
        id: role.id,
      });

      resetForm();

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

      <div className="border-b p-5 flex justify-between">
        <h2 className="text-xl font-bold">
          {isCreate && "Create Role"}
          {isEdit && "Edit Role"}
          {isView && "Role Details"}
        </h2>

        <button
          onClick={onClose}
          className="text-xl"
        >
          ✕
        </button>
      </div>

      {/* Form */}

      <div className="p-6 grid md:grid-cols-2 gap-4">

        {/* Role Name */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Role Name <span className="text-red-500">*</span>
          </label>

          <input
            name="roleName"
            value={formData.roleName}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Role Name"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
              !isView && formData.roleName.trim() === ""
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          />
        </div>

        {/* Description */}

        <div>
          <label className="mb-2 block text-sm font-medium">
            Description <span className="text-red-500">*</span>
          </label>

          <input
            name="description"
            value={formData.description}
            onChange={handleChange}
            disabled={isView}
            placeholder="Enter Description"
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
              !isView && formData.description.trim() === ""
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
            disabled={isView}
            value={formData.organizationModel?.id || ""}
            onChange={(e) => {
              const selected = organizations.find(
                (org) => org.id === e.target.value
              );

              setFormData((prev) => ({
                ...prev,
                organizationModel: selected,
              }));
            }}
            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
              !isView && !formData.organizationModel
                ? "border-gray-300 focus:ring-red-400"
                : "border-gray-300 focus:ring-blue-500"
            }`}
          >
            <option value="">Select Organization</option>

            {organizations.map((org) => (
              <option
                key={org.id}
                value={org.id}
              >
                {org.organizationName}
              </option>
            ))}
          </select>
        </div>


        {/* Active */}

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
            Active Role
          </label>
        </div>

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
            disabled={!isFormValid}
            className={`px-5 py-2 rounded-lg text-white transition ${
              isFormValid
                ? "bg-green-600 hover:bg-green-700"
                : "bg-gray-400 cursor-not-allowed"
            }`}
          >
            Save
          </button>
        )}

        {isEdit && (
          <button
            onClick={handleUpdate}
            disabled={!isFormValid}
            className={`px-5 py-2 rounded-lg text-white transition ${
              isFormValid
                ? "bg-blue-600 hover:bg-blue-700"
                : "bg-gray-400 cursor-not-allowed"
            }`}
          >
            Update
          </button>
        )}

      </div>

    </div>
  </div>
);
}