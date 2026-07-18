import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function CategoryModalBox({
    isOpen,
    onClose,
    mode,
    category,
    onSuccess,
}) {

    const isView = mode === "view";
    const isEdit = mode === "edit";
    const isCreate = mode === "create";

    const [organizations, setOrganizations] = useState([]);
    const [branches, setBranches] = useState([]);

    const [formData, setFormData] = useState({
        categoryCode: "",
        categoryName: "",
        description: "",
        imageUrl: "",
        displayOrder: "",
        available: true,
        organizationModel: null,
        branchModel: null,
        isActive: true,
    });

    useEffect(() => {
        if (isOpen) {
            loadOrganizations();
            loadBranches();
        }
    }, [isOpen]);

    useEffect(() => {
        if (category) {
            setFormData({
                id: category.id,
                categoryCode: category.categoryCode || "",
                categoryName: category.categoryName || "",
                description: category.description || "",
                imageUrl: category.imageUrl || "",
                displayOrder: category.displayOrder || "",
                available: category.available ?? true,
                organizationModel: category.organizationModel || null,
                branchModel: category.branchModel || null,
                isActive: category.isActive,
            });
        } else {
            resetForm();
        }
    }, [category]);

    const handleSave = async () => {
    try {

        await axiosClient.post(
            "/category",
            formData
        );

        onSuccess();
        onClose();

    } catch(error) {

        console.error(error);

        alert(
            error?.response?.data?.message ||
            "Unable to save category."
        );
    }
};

    const resetForm = () => {
    setFormData({
        id: null,
        categoryCode: "",
        categoryName: "",
        description: "",
        imageUrl: "",
        displayOrder: "",
        available: true,
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

            setOrganizations(response.data.data.content || []);
        } catch (error) {
            console.error("Failed to load organizations", error);
        }
    };

    const loadBranches = async () => {
        try {
            const response = await axiosClient.post(
                "/branch/search?page=0&size=100",
                {}
            );

            setBranches(response.data.data.content || []);
        } catch (error) {
            console.error("Failed to load branches", error);
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
            (org) => org.id === e.target.value
        );

        setFormData((prev) => ({
            ...prev,
            organizationModel: selected || null,
            branchModel: null,
        }));
    };

    const handleBranchChange = (e) => {
        const selected = branches.find(
            (branch) => branch.id === e.target.value
        );

        setFormData((prev) => ({
            ...prev,
            branchModel: selected || null,
        }));
    };

    const fileToDataUrl = (file) =>
    new Promise((resolve, reject) => {

        const reader = new FileReader();

        reader.onload = () => resolve(reader.result);

        reader.onerror = () =>
            reject(new Error("Failed to read image"));

        reader.readAsDataURL(file);
    });


const handleImageChange = async (e) => {

    const file = e.target.files[0];

    if (!file) return;


    try {

        const dataUrl = await fileToDataUrl(file);


        setFormData((prev) => ({
            ...prev,
            imageUrl: dataUrl
        }));


    } catch(error) {

        console.log(error);
    }
};

   const handleUpdate = async () => {

    try {

        await axiosClient.put(
            `/category/${formData.id}`,
            formData
        );


        onSuccess();
        onClose();

    } catch(error) {

        console.error(error);

        alert(
            error?.response?.data?.message ||
            "Unable to update category."
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
                        {isCreate && "Create Category"}
                        {isEdit && "Edit Category"}
                        {isView && "Category Details"}
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

                    {/* Category Code */}

                    <div>
                        <label className="block mb-2 text-sm font-medium">
                            Category Code
                        </label>

                        <input
                            type="text"
                            name="categoryCode"
                            value={formData.categoryCode}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Category Code"
                            className="w-full rounded-lg border px-4 py-3"
                        />
                    </div>

                    {/* Category Name */}

                    <div>
                        <label className="block mb-2 text-sm font-medium">
                            Category Name
                        </label>

                        <input
                            type="text"
                            name="categoryName"
                            value={formData.categoryName}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Category Name"
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
                            placeholder="Display Order"
                            className="w-full rounded-lg border px-4 py-3"
                        />
                    </div>

                    {/* Available */}

                    <div>
                        <label className="block mb-2 text-sm font-medium">
                            Available
                        </label>

                        <select
                            name="available"
                            value={String(formData.available)}
                            onChange={(e) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    available: e.target.value === "true",
                                }))
                            }
                            disabled={isView}
                            className="w-full rounded-lg border px-4 py-3"
                        >
                            <option value="true">Available</option>
                            <option value="false">Unavailable</option>
                        </select>
                    </div>

                    {/* Description */}

                    <div className="md:col-span-2">

                        <label className="block mb-2 text-sm font-medium">
                            Description
                        </label>

                        <textarea
                            rows={4}
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Description"
                            className="w-full rounded-lg border px-4 py-3 resize-none"
                        />

                    </div>

                    {/* Image Upload */}

                    <div className="md:col-span-2">

    <label className="block mb-2 text-sm font-medium">
        Category Image
    </label>


    {!isView && (
        <input
            type="file"
            accept="image/*"
            onChange={handleImageChange}
            className="w-full rounded-lg border px-4 py-3"
        />
    )}


    {formData.imageUrl && (
        <img
            src={formData.imageUrl}
            alt="Category"
            className="mt-3 h-32 w-32 rounded-lg border object-cover"
        />
    )}

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

                            Active Category

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
    onClick={isCreate ? handleSave : handleUpdate}
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