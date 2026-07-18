import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";

export default function EmployeeModalBox({
    isOpen,
    onClose,
    mode,
    employee,
    onSuccess,
}) {

    const isView = mode === "view";
    const isEdit = mode === "edit";
    const isCreate = mode === "create";

    const [organizations, setOrganizations] = useState([]);
    const [branches, setBranches] = useState([]);
    const [roles, setRoles] = useState([]);

    const [formData, setFormData] = useState({
        fullName: "",
        email: "",
        phone: "",
        password: "",
        cnic: "",
        address: "",
        emergencyContact: "",
        designation: "",
        salary: "",
        joiningDate: "",
        employmentStatus: "ACTIVE",
        referredBy: "",
        referralCode: "",
        organizationModel: null,
        branchModel: null,
        role: null,
        isActive: true,
    });

    const isFormValid =
        formData.fullName.trim() !== "" &&
        formData.email.trim() !== "" &&
        formData.phone.trim() !== "" &&
        (isEdit || formData.password.trim() !== "") &&
        formData.cnic.trim() !== "" &&
        formData.address.trim() !== "" &&
        formData.emergencyContact.trim() !== "" &&
        formData.designation.trim() !== "" &&
        formData.salary !== "" &&
        formData.joiningDate !== "" &&
        formData.employmentStatus !== "" &&
        formData.referredBy.trim() !== "" &&
        formData.referralCode.trim() !== "" &&
        formData.organizationModel &&
        formData.branchModel &&
        formData.role;

    const resetForm = () => {
        setFormData({
            fullName: "",
            email: "",
            phone: "",
            password: "",
            cnic: "",
            address: "",
            emergencyContact: "",
            designation: "",
            salary: "",
            joiningDate: "",
            employmentStatus: "ACTIVE",
            referredBy: "",
            referralCode: "",
            organizationModel: null,
            branchModel: null,
            role: null,
            isActive: true,
        });
    };

    useEffect(() => {

        if (isOpen) {

            loadOrganizations();
            loadBranches();
            loadRoles();

        }

    }, [isOpen]);

    useEffect(() => {

        if (employee) {

            setFormData({

                id: employee.id,

                fullName:
                    employee.fullName || "",

                email:
                    employee.userModel?.email || "",

                phone:
                    employee.phone || "",

                password: "",

                cnic:
                    employee.cnic || "",

                address:
                    employee.address || "",

                emergencyContact:
                    employee.emergencyContact || "",

                designation:
                    employee.designation || "",

                salary:
                    employee.salary || "",

                joiningDate:
                    employee.joiningDate || "",

                employmentStatus:
                    employee.employmentStatus || "ACTIVE",

                referredBy:
                    employee.referredBy || "",

                referralCode:
                    employee.referralCode || "",

                organizationModel:
                    employee.organizationModel || null,

                branchModel:
                    employee.branchModel || null,

                role:
                    employee.roleModel || null,

                isActive:
                    employee.isActive,

            });

        } else {

            resetForm();

        }

    }, [employee]);

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

    const loadRoles = async () => {

        try {

            const response = await axiosClient.post(
                "/role/search?page=0&size=100",
                {}
            );

            setRoles(
                response.data.data.content || []
            );

        } catch (error) {

            console.error(
                "Failed to load roles",
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

            role: null,

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

    const handleRoleChange = (e) => {

        const selected = roles.find(
            (role) => role.id === e.target.value
        );

        setFormData((prev) => ({

            ...prev,

            role:
                selected || null,

        }));

    };

    const handleSave = async () => {

        try {

            await axiosClient.post(
                "/employee",
                formData
            );

            onSuccess();

            resetForm();

            onClose();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to save employee."
            );

        }

    };

    const handleUpdate = async () => {

        try {

            await axiosClient.put(
                `/employee/${employee.id}`,
                formData
            );

            onSuccess();

            resetForm();

            onClose();

        } catch (error) {

            console.error(error);

            alert(
                error?.response?.data?.message ||
                "Unable to update employee."
            );

        }

    };


    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
            <div className="w-full max-w-6xl max-h-[90vh] overflow-y-auto rounded-2xl bg-white shadow-xl">

                {/* Header */}

                <div className="flex items-center justify-between border-b p-5">

                    <h2 className="text-xl font-bold">

                        {isCreate && "Create Employee"}

                        {isEdit && "Edit Employee"}

                        {isView && "Employee Details"}

                    </h2>

                    <button
                        onClick={onClose}
                        className="text-2xl text-gray-500 hover:text-black"
                    >
                        ×
                    </button>

                </div>

                {/* Body */}

                <div className="grid md:grid-cols-2 gap-5 p-6">

                    {/* Full Name */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Full Name <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="fullName"
                            value={formData.fullName}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Full Name"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.fullName.trim()
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Email */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Email <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Email"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.email.trim()
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Password */}

                    {isCreate && (

                        <div>

                            <label className="block mb-2 text-sm font-medium">
                                Password <span className="text-red-500">*</span>
                            </label>

                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                placeholder="Enter Password"
                                className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.password.trim()
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                                }`}
                            />

                        </div>

                    )}

                    {/* Phone */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Phone <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="phone"
                            value={formData.phone}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="03XXXXXXXXX"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.phone.trim()
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* CNIC */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            CNIC <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="cnic"
                            value={formData.cnic}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="35202-1234567-1"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.cnic.trim()
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Address */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
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
                            !isView && !formData.address.trim()
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Emergency Contact */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Emergency Contact <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="emergencyContact"
                            value={formData.emergencyContact}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Emergency Contact"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.emergencyContact.trim()
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>


                    {/* Designation */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Designation <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="designation"
                            value={formData.designation}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Designation"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.designation.trim()
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Salary */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Salary <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="number"
                            name="salary"
                            value={formData.salary}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Salary"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && formData.salary === ""
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Joining Date */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Joining Date <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="date"
                            name="joiningDate"
                            value={formData.joiningDate}
                            onChange={handleChange}
                            disabled={isView}
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && formData.joiningDate
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Employment Status */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Employment Status <span className="text-red-500">*</span>
                        </label>

                        <select
                            name="employmentStatus"
                            value={formData.employmentStatus}
                            onChange={handleChange}
                            disabled={isView}
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && formData.employmentStatus
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        >
                            <option value="ACTIVE">ACTIVE</option>
                            <option value="ON_LEAVE">ON LEAVE</option>
                            <option value="RESIGNED">RESIGNED</option>
                            <option value="TERMINATED">TERMINATED</option>
                        </select>

                    </div>

                    {/* Referred By */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Referred By <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="referredBy"
                            value={formData.referredBy}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Enter Referrer Name"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.referredBy.trim()
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        />

                    </div>

                    {/* Referral Code */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Referral Code <span className="text-red-500">*</span>
                        </label>

                        <input
                            type="text"
                            name="referralCode"
                            value={formData.referralCode}
                            onChange={handleChange}
                            disabled={isView}
                            placeholder="Referral Code"
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.referralCode.trim()
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
                            !isView && !formData.organizationModel
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
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
                            Branch <span className="text-red-500">*</span>
                        </label>

                        <select
                            value={formData.branchModel?.id || ""}
                            onChange={handleBranchChange}
                            disabled={isView}
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.branchModel
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

                    {/* Role */}

                    <div>

                        <label className="block mb-2 text-sm font-medium">
                            Role <span className="text-red-500">*</span>
                        </label>

                        <select
                            value={formData.role?.id || ""}
                            onChange={handleRoleChange}
                            disabled={isView}
                            className={`w-full rounded-lg border px-4 py-3 focus:outline-none focus:ring-2 ${
                            !isView && !formData.role
                                ? "border-gray-300 focus:ring-red-400"
                                : "border-gray-300 focus:ring-blue-500"
                            }`}
                        >

                            <option value="">
                                Select Role
                            </option>

                            {roles
                                .filter(
                                    (role) =>
                                        !formData.organizationModel ||
                                        role.organizationModel?.id ===
                                            formData.organizationModel?.id
                                )
                                .map((role) => (

                                    <option
                                        key={role.id}
                                        value={role.id}
                                    >
                                        {role.roleName}
                                    </option>

                                ))}

                        </select>

                    </div>

                    {/* Active */}

                    <div className="flex items-center mt-8">

                        <label className="flex items-center gap-3">

                            <input
                                type="checkbox"
                                name="isActive"
                                checked={formData.isActive}
                                onChange={handleChange}
                                disabled={isView}
                            />

                            Active Employee

                        </label>

                    </div>

                </div>

                {/* Footer */}

                <div className="flex flex-col sm:flex-row justify-end gap-3 border-t p-5">

                    <button
                        onClick={onClose}
                        className="w-full sm:w-auto rounded-lg border px-5 py-2 hover:bg-gray-100"
                    >
                        Close
                    </button>

                    {!isView && (

                        <button
                            onClick={
                                isCreate
                                    ? handleSave
                                    : handleUpdate
                            }
                            disabled={!isFormValid}
                            className={`w-full sm:w-auto rounded-lg px-5 py-2 text-white transition ${
                                !isFormValid
                                    ? "bg-gray-400 cursor-not-allowed"
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