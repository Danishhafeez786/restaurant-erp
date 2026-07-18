import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function ModifierGroupModelBox({
    isOpen,
    onClose,
    mode,
    modifierGroup,
    onSuccess,
}) {

    const isView = mode === "view";
    const isEdit = mode === "edit";
    const isCreate = mode === "create";

    const [organizations, setOrganizations] = useState([]);
    const [branches, setBranches] = useState([]);

    const [formData, setFormData] = useState({
        code: "",
        name: "",
        description: "",
        minSelection: "",
        maxSelection: "",
        required: false,
        organizationModel: null,
        branchModel: null,
        isActive: true,
    });

    /* =========================
       Required Field Validation
    ========================== */

    const isFormValid =
        formData.code.trim() !== "" &&
        formData.name.trim() !== "" &&
        formData.description.trim() !== "" &&
        formData.minSelection !== "" &&
        formData.maxSelection !== "" &&
        formData.organizationModel !== null &&
        formData.branchModel !== null;

    /* =========================
       Reset Form
    ========================== */

    const resetForm = () => {
        setFormData({
            code: "",
            name: "",
            description: "",
            minSelection: "",
            maxSelection: "",
            required: false,
            organizationModel: null,
            branchModel: null,
            isActive: true,
        });
    };

    /* =========================
       useEffect
    ========================== */

    useEffect(() => {
        if (isOpen) {
            loadOrganizations();
            loadBranches();
        }
    }, [isOpen]);

    useEffect(() => {
        if (modifierGroup) {
            setFormData({
                id: modifierGroup.id,
                code: modifierGroup.code || "",
                name: modifierGroup.name || "",
                description: modifierGroup.description || "",
                minSelection: modifierGroup.minSelection ?? "",
                maxSelection: modifierGroup.maxSelection ?? "",
                required: modifierGroup.required ?? false,
                organizationModel:
                    modifierGroup.organizationModel || null,
                branchModel:
                    modifierGroup.branchModel || null,
                isActive: modifierGroup.isActive,
            });
        } else {
            resetForm();
        }
    }, [modifierGroup, mode]);

    /* =========================
       Load Organizations
    ========================== */

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

    /* =========================
       Load Branches
    ========================== */

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

    /* =========================
       Handle Change
    ========================== */

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

    /* =========================
       Handle Organization
    ========================== */

    const handleOrganizationChange = (e) => {

        const selected = organizations.find(
            (organization) =>
                String(organization.id) === e.target.value
        );

        setFormData((prev) => ({
            ...prev,
            organizationModel: selected || null,
            branchModel: null,
        }));
    };

    /* =========================
       Handle Branch
    ========================== */

    const handleBranchChange = (e) => {

        const selected = branches.find(
            (branch) =>
                String(branch.id) === e.target.value
        );

        setFormData((prev) => ({
            ...prev,
            branchModel: selected || null,
        }));
    };

    /* =========================
       Save
    ========================== */

    const handleSave = async () => {

        try {

            const payload = {
                code: formData.code,
                name: formData.name,
                description: formData.description,
                minSelection: Number(formData.minSelection),
                maxSelection: Number(formData.maxSelection),
                required: formData.required,
                organizationModel: formData.organizationModel,
                branchModel: formData.branchModel,
                isActive: formData.isActive,
            };

            await axiosClient.post(
                "/modifier-group",
                payload
            );

            resetForm();
            onSuccess();
            onClose();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to save modifier group."
            );
        }
    };

    /* =========================
       Update
    ========================== */

    const handleUpdate = async () => {

        try {

            const payload = {
                code: formData.code,
                name: formData.name,
                description: formData.description,
                minSelection: Number(formData.minSelection),
                maxSelection: Number(formData.maxSelection),
                required: formData.required,
                organizationModel: formData.organizationModel,
                branchModel: formData.branchModel,
                isActive: formData.isActive,
            };

            await axiosClient.put(
                `/modifier-group/${modifierGroup.id}`,
                payload
            );

            resetForm();
            onSuccess();
            onClose();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to update modifier group."
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

                    {isCreate && "Create Modifier Group"}

                    {isEdit && "Edit Modifier Group"}

                    {isView && "Modifier Group Details"}

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

                {/* Code */}

                <div>

                    <label className="block mb-2 text-sm font-medium">
                        Code <span className="text-red-500">*</span>
                    </label>

                    <input
                        type="text"
                        name="code"
                        value={formData.code}
                        onChange={handleChange}
                        disabled={isView}
                        placeholder="Enter Code"
                        className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !formData.code && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                        }`}
                    />

                </div>

                {/* Name */}

                <div>

                    <label className="block mb-2 text-sm font-medium">
                        Name <span className="text-red-500">*</span>
                    </label>

                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        disabled={isView}
                        placeholder="Enter Name"
                        className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !formData.name && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                        }`}
                    />

                </div>

                {/* Description */}

                <div className="md:col-span-2">

                    <label className="block mb-2 text-sm font-medium">
                        Description <span className="text-red-500">*</span>
                    </label>

                    <textarea
                        rows={4}
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        disabled={isView}
                        placeholder="Enter Description"
                        className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 resize-none ${
                            !formData.description && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                        }`}
                    />

                </div>

                {/* Min Selection */}

                <div>

                    <label className="block mb-2 text-sm font-medium">
                        Min Selection <span className="text-red-500">*</span>
                    </label>

                    <input
                        type="number"
                        name="minSelection"
                        value={formData.minSelection}
                        onChange={handleChange}
                        disabled={isView}
                        placeholder="Enter Minimum Selection"
                        className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            formData.minSelection === "" && !isView
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                        }`}
                    />

                </div>

                {/* Max Selection */}

                <div>

                    <label className="block mb-2 text-sm font-medium">
                        Max Selection <span className="text-red-500">*</span>
                    </label>

                    <input
                        type="number"
                        name="maxSelection"
                        value={formData.maxSelection}
                        onChange={handleChange}
                        disabled={isView}
                        placeholder="Enter Maximum Selection"
                        className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            formData.maxSelection === "" && !isView
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


                {/* Required */}

                <div>

                    <label className="flex items-center gap-3 mt-2 text-sm font-medium text-gray-700">

                        <input
                            type="checkbox"
                            name="required"
                            checked={formData.required}
                            onChange={handleChange}
                            disabled={isView}
                            className="h-4 w-4"
                        />

                        Required Modifier Group

                    </label>

                </div>

                {/* Active */}

                <div>

                    <label className="flex items-center gap-3 mt-2 text-sm font-medium text-gray-700">

                        <input
                            type="checkbox"
                            name="isActive"
                            checked={formData.isActive}
                            onChange={handleChange}
                            disabled={isView}
                            className="h-4 w-4"
                        />

                        Active Modifier Group

                    </label>

                </div>

            </div>

            {/* ====================== FOOTER ====================== */}

            <div className="flex flex-col sm:flex-row justify-end gap-3 border-t p-5">

                <button
                    onClick={onClose}
                    className="w-full sm:w-auto rounded-lg border px-5 py-2 hover:bg-gray-100 transition"
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