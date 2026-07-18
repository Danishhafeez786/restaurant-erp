import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function TableManagementModelBox({
    isOpen,
    onClose,
    mode,
    tableData,
    onSuccess,
}) {

    const isView = mode === "view";
    const isEdit = mode === "edit";
    const isCreate = mode === "create";

    const [organizations, setOrganizations] = useState([]);
    const [branches, setBranches] = useState([]);

    const [formData, setFormData] = useState({
        tableNumber: "",
        tableName: "",
        capacity: "",
        qrToken: "",
        organizationModel: null,
        branchModel: null,
        isActive: true,
    });

    const isFormValid =
        formData.tableNumber.trim() !== "" &&
        formData.tableName.trim() !== "" &&
        formData.capacity !== "" &&
        formData.qrToken.trim() !== "" &&
        formData.organizationModel !== null &&
        formData.branchModel !== null;

    useEffect(() => {

        if (isOpen) {

            loadOrganizations();

            loadBranches();

        }

    }, [isOpen]);

    useEffect(() => {

        if (tableData) {

            setFormData({

                id: tableData.id,

                tableNumber: tableData.tableNumber || "",

                tableName: tableData.tableName || "",

                capacity: tableData.capacity || "",

                qrToken: tableData.qrToken || "",

                organizationModel:
                    tableData.organizationModel || null,

                branchModel:
                    tableData.branchModel || null,

                isActive:
                    tableData.isActive,

            });

        } else {

            resetForm();

        }

    }, [tableData]);

    const resetForm = () => {

        setFormData({

            tableNumber: "",

            tableName: "",

            capacity: "",

            qrToken: "",

            organizationModel: null,

            branchModel: null,

            isActive: true,

        });

    };

    const loadOrganizations = async () => {

        try {

            const response = await axiosClient.post(
                "/organization/search?page=0&size=100",
                {}
            );

            setOrganizations(
                response.data.data.content || []
            );

        } catch (error) {

            console.error(
                "Failed to load organizations",
                error
            );

        }

    };

    const loadBranches = async () => {

        try {

            const response = await axiosClient.post(
                "/branch/search?page=0&size=100",
                {}
            );

            setBranches(
                response.data.data.content || []
            );

        } catch (error) {

            console.error(
                "Failed to load branches",
                error
            );

        }

    };

    const handleChange = (e) => {

        const {
            name,
            value,
            checked,
            type,
        } = e.target;

        setFormData((prev) => ({

            ...prev,

            [name]:
                type === "checkbox"
                    ? checked
                    : value,

        }));

    };

    const handleOrganizationChange = (e) => {

        const selected = organizations.find(
            (org) => org.id === e.target.value
        );

        setFormData((prev) => ({

            ...prev,

            organizationModel:
                selected || null,

            branchModel: null,

        }));

    };

    const handleBranchChange = (e) => {

        const selected = branches.find(
            (branch) => branch.id === e.target.value
        );

        setFormData((prev) => ({

            ...prev,

            branchModel:
                selected || null,

        }));

    };

    const handleSave = async () => {

        try {

            const payload = {

                tableNumber:
                    formData.tableNumber,

                tableName:
                    formData.tableName,

                capacity:
                    Number(formData.capacity),

                qrToken:
                    formData.qrToken,

                organizationModel:
                    formData.organizationModel,

                branchModel:
                    formData.branchModel,

                isActive:
                    formData.isActive,

            };

            await axiosClient.post(
                "/table-management",
                payload
            );

            resetForm();

            onSuccess();

            onClose();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to save table."
            );

        }

    };

    const handleUpdate = async () => {

        try {

            const payload = {

                tableNumber:
                    formData.tableNumber,

                tableName:
                    formData.tableName,

                capacity:
                    Number(formData.capacity),

                qrToken:
                    formData.qrToken,

                organizationModel:
                    formData.organizationModel,

                branchModel:
                    formData.branchModel,

                isActive:
                    formData.isActive,

            };

            await axiosClient.put(
                `/table-management/${tableData.id}`,
                payload
            );

            resetForm();

            onSuccess();

            onClose();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to update table."
            );

        }

    };

    if (!isOpen) return null;


    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">

            <div className="w-full max-w-5xl max-h-[90vh] overflow-y-auto rounded-2xl bg-white shadow-xl">

                {/* ====================== HEADER ====================== */}

                <div className="flex items-center justify-between border-b p-5">

                    <h2 className="text-xl font-bold">

                        {isCreate && "Create Table"}

                        {isEdit && "Edit Table"}

                        {isView && "Table Details"}

                    </h2>

                    <button
                        onClick={onClose}
                        className="text-2xl text-gray-500 hover:text-black"
                    >
                        ×
                    </button>

                </div>

                {/* ====================== BODY ====================== */}

                <div className="grid grid-cols-1 md:grid-cols-2 gap-5 p-6">

                    {/* Table Number */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Table Number <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="tableNumber"
                            value={formData.tableNumber}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Table Number"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !formData.tableNumber && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Table Name */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Table Name <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="tableName"
                            value={formData.tableName}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Table Name"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !formData.tableName && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Capacity */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Capacity <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="number"
                            name="capacity"
                            value={formData.capacity}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Capacity"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !formData.capacity === "" && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* QR Token */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            QR Token <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="qrToken"
                            value={formData.qrToken}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter QR Token"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !formData.qrToken && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Organization */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Organization <span className="text-red-500">*</span>
                        </label>

                        <select
                            value={formData.organizationModel?.id || ""}
                            onChange={handleOrganizationChange}
                            disabled={isView}
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !formData.organizationModel && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        >

                            <option value="">
                                Select Organization
                            </option>

                            {organizations.map((organization) => (

                                <option
                                    key={organization.id}
                                    value={organization.id}
                                >
                                    {organization.organizationName}
                                </option>

                            ))}

                        </select>

                    </div>

                    {/* Branch */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Branch <span className="text-red-500">*</span>
                        </label>

                        <select
                            value={formData.branchModel?.id || ""}
                            onChange={handleBranchChange}
                            disabled={isView}
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !formData.branchModel && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        >

                            <option value="">
                                Select Branch
                            </option>

                            {branches
                                .filter(
                                    (branch) =>
                                        !formData.organizationModel ||
                                        branch.organizationModel?.id ===
                                            formData.organizationModel?.id
                                )
                                .map((branch) => (

                                    <option
                                        key={branch.id}
                                        value={branch.id}
                                    >
                                        {branch.branchName}
                                    </option>

                                ))}

                        </select>

                    </div>


                    {/* Active */}

                    <div className="md:col-span-2 flex items-center mt-2">

                        <label className="flex items-center gap-3">

                            <input
                                type="checkbox"
                                name="isActive"
                                checked={formData.isActive}
                                onChange={handleChange}
                                disabled={isView}
                            />

                            Active Table

                        </label>

                    </div>

                </div>

                {/* ====================== FOOTER ====================== */}

                <div className="flex flex-col sm:flex-row justify-end gap-3 border-t p-5">

                    <button
                        onClick={onClose}
                        className="w-full sm:w-auto rounded-lg border px-5 py-2 hover:bg-gray-100"
                    >
                        Close
                    </button>

                    {isCreate && (

                        <button
                            onClick={handleSave}
                            disabled={!isFormValid}
                            className={`w-full sm:w-auto rounded-lg px-5 py-2 text-white font-medium transition ${
                                isFormValid
                                    ? "bg-green-600 hover:bg-green-700"
                                    : "bg-gray-400 cursor-not-allowed opacity-70"
                            }`}
                        >
                            Save
                        </button>

                    )}

                    {isEdit && (

                        <button
                            onClick={handleUpdate}
                            disabled={!isFormValid}
                            className={`w-full sm:w-auto rounded-lg px-5 py-2 text-white font-medium transition ${
                                isFormValid
                                    ? "bg-blue-600 hover:bg-blue-700"
                                    : "bg-gray-400 cursor-not-allowed opacity-70"
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