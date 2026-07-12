import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function ModifierModelBox({
    isOpen,
    onClose,
    mode,
    modifier,
    onSuccess,
}) {

    const isView = mode === "view";
    const isEdit = mode === "edit";
    const isCreate = mode === "create";

    const [modifierGroups, setModifierGroups] = useState([]);

    const [organizations, setOrganizations] = useState([]);

    const [branches, setBranches] = useState([]);

    const [formData, setFormData] = useState({

        code: "",

        name: "",

        sku: "",

        price: "",

        costPrice: "",

        calories: "",

        displayOrder: "",

        inventoryTracked: false,

        available: true,

        modifierGroupModel: null,

        organizationModel: null,

        branchModel: null,

        isActive: true,

    });

    useEffect(() => {

        if (isOpen) {

            loadModifierGroups();

            loadOrganizations();

            loadBranches();

        }

    }, [isOpen]);

    useEffect(() => {

        if (modifier) {

            setFormData({

                id: modifier.id,

                code:
                    modifier.code || "",

                name:
                    modifier.name || "",

                sku:
                    modifier.sku || "",

                price:
                    modifier.price ?? "",

                costPrice:
                    modifier.costPrice ?? "",

                calories:
                    modifier.calories ?? "",

                displayOrder:
                    modifier.displayOrder ?? "",

                inventoryTracked:
                    modifier.inventoryTracked ?? false,

                available:
                    modifier.available ?? true,

                modifierGroupModel:
                    modifier.modifierGroupModel || null,

                organizationModel:
                    modifier.organizationModel || null,

                branchModel:
                    modifier.branchModel || null,

                isActive:
                    modifier.isActive,

            });

        } else {

            resetForm();

        }

    }, [modifier]);

    const resetForm = () => {

        setFormData({

            code: "",

            name: "",

            sku: "",

            price: "",

            costPrice: "",

            calories: "",

            displayOrder: "",

            inventoryTracked: false,

            available: true,

            modifierGroupModel: null,

            organizationModel: null,

            branchModel: null,

            isActive: true,

        });

    };

    const loadModifierGroups = async () => {

        try {

            const response = await axiosClient.post(
                "/modifier-group/search?page=0&size=100",
                {}
            );

            setModifierGroups(
                response.data.data.content || []
            );

        } catch (error) {

            console.error(
                "Failed to load modifier groups",
                error
            );

        }

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

    const handleModifierGroupChange = (e) => {

        const selected = modifierGroups.find(
            (group) =>
                group.id === e.target.value
        );

        setFormData((prev) => ({

            ...prev,

            modifierGroupModel:
                selected || null,

        }));

    };

    const handleOrganizationChange = (e) => {

        const selected = organizations.find(
            (organization) =>
                organization.id === e.target.value
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
            (branch) =>
                branch.id === e.target.value
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

                code:
                    formData.code,

                name:
                    formData.name,

                sku:
                    formData.sku,

                price:
                    Number(formData.price),

                costPrice:
                    Number(formData.costPrice),

                calories:
                    Number(formData.calories),

                displayOrder:
                    Number(formData.displayOrder),

                inventoryTracked:
                    formData.inventoryTracked,

                available:
                    formData.available,

                modifierGroupModel:
                    formData.modifierGroupModel,

                organizationModel:
                    formData.organizationModel,

                branchModel:
                    formData.branchModel,

                isActive:
                    formData.isActive,

            };

            if (isCreate) {

                await axiosClient.post(
                    "/modifier",
                    payload
                );

            } else {

                await axiosClient.put(
                    `/modifier/${modifier.id}`,
                    payload
                );

            }

            onSuccess();

            onClose();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to save modifier."
            );

        }

    };

    if (!isOpen) return null;
        return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">

            <div className="w-full max-w-6xl max-h-[90vh] overflow-y-auto rounded-2xl bg-white shadow-xl">

                {/* ====================== HEADER ====================== */}

                <div className="flex items-center justify-between border-b p-5">

                    <h2 className="text-xl font-bold">

                        {isCreate && "Create Modifier"}

                        {isEdit && "Edit Modifier"}

                        {isView && "Modifier Details"}

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
                            Code
                        </label>

                        <input
                            type="text"
                            name="code"
                            value={formData.code}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Code"
                            className="w-full rounded-lg border px-4 py-3"
                        />

                    </div>

                    {/* Name */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Name
                        </label>

                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Name"
                            className="w-full rounded-lg border px-4 py-3"
                        />

                    </div>

                    {/* SKU */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            SKU
                        </label>

                        <input
                            type="text"
                            name="sku"
                            value={formData.sku}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter SKU"
                            className="w-full rounded-lg border px-4 py-3"
                        />

                    </div>

                    {/* Price */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Price
                        </label>

                        <input
                            type="number"
                            name="price"
                            value={formData.price}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Price"
                            className="w-full rounded-lg border px-4 py-3"
                        />

                    </div>

                    {/* Cost Price */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Cost Price
                        </label>

                        <input
                            type="number"
                            name="costPrice"
                            value={formData.costPrice}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Cost Price"
                            className="w-full rounded-lg border px-4 py-3"
                        />

                    </div>

                    {/* Calories */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Calories
                        </label>

                        <input
                            type="number"
                            name="calories"
                            value={formData.calories}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Calories"
                            className="w-full rounded-lg border px-4 py-3"
                        />

                    </div>

                    {/* Display Order */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Display Order
                        </label>

                        <input
                            type="number"
                            name="displayOrder"
                            value={formData.displayOrder}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Display Order"
                            className="w-full rounded-lg border px-4 py-3"
                        />

                    </div>

                    {/* Modifier Group */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Modifier Group
                        </label>

                        <select
                            value={formData.modifierGroupModel?.id || ""}
                            onChange={handleModifierGroupChange}
                            disabled={isView}
                            className="w-full rounded-lg border px-4 py-3"
                        >

                            <option value="">
                                Select Modifier Group
                            </option>

                            {modifierGroups.map((group) => (

                                <option
                                    key={group.id}
                                    value={group.id}
                                >
                                    {group.name}
                                </option>

                            ))}

                        </select>

                    </div>

                    {/* Organization */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Organization
                        </label>

                        <select
                            value={formData.organizationModel?.id || ""}
                            onChange={handleOrganizationChange}
                            disabled={isView}
                            className="w-full rounded-lg border px-4 py-3"
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
                            Branch
                        </label>

                        <select
                            value={formData.branchModel?.id || ""}
                            onChange={handleBranchChange}
                            disabled={isView}
                            className="w-full rounded-lg border px-4 py-3"
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

                    {/* Inventory Tracked */}

                    <div>

                        <label className="flex items-center gap-3 mt-2">

                            <input
                                type="checkbox"
                                name="inventoryTracked"
                                checked={formData.inventoryTracked}
                                onChange={handleChange}
                                disabled={isView}
                            />

                            Inventory Tracked

                        </label>

                    </div>

                    {/* Available */}

                    <div>

                        <label className="flex items-center gap-3 mt-2">

                            <input
                                type="checkbox"
                                name="available"
                                checked={formData.available}
                                onChange={handleChange}
                                disabled={isView}
                            />

                            Available

                        </label>

                    </div>

                    {/* Active */}

                    <div className="md:col-span-2">

                        <label className="flex items-center gap-3 mt-2">

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

                {/* ====================== FOOTER ====================== */}

                <div className="flex flex-col sm:flex-row justify-end gap-3 border-t p-5">

                    <button
                        onClick={onClose}
                        className="w-full sm:w-auto rounded-lg border px-5 py-2 hover:bg-gray-100"
                    >
                        Close
                    </button>

                    {!isView && (

                        <button
                            onClick={handleSave}
                            className={`w-full sm:w-auto rounded-lg px-5 py-2 text-white ${
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