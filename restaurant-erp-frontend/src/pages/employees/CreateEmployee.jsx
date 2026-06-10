import { useState } from "react";
import Sidebar from "../../components/Sidebar";

export default function CreateEmployee() {
  const [image, setImage] = useState(null);

  return (
    <div className="min-h-screen bg-gray-100 lg:flex">
        <Sidebar />
    <div className="p-4 md:p-6 bg-slate-100 min-h-screen">
      <h1 className="text-3xl font-bold mb-6">
        Add New Employee
      </h1>

      <form className="space-y-5">

        {/* Personal Section */}
        <div className="grid lg:grid-cols-4 gap-5">

          <div className="lg:col-span-3 bg-white rounded-xl shadow p-5">
            <h2 className="font-semibold text-lg mb-5">
              Core Personal & Profile
            </h2>

            <div className="grid md:grid-cols-2 xl:grid-cols-4 gap-4">

              <Input label="First Name" />
              <Input label="Last Name" />

              <Input label="Father Name" />
              <Input label="CNIC" />

              <Input
                label="Date Of Birth"
                type="date"
              />

              <Select
                label="Gender"
                options={[
                  "MALE",
                  "FEMALE",
                  "OTHER",
                ]}
              />
            </div>
          </div>

          {/* Image Upload */}
          <div className="bg-white rounded-xl shadow p-5">
            <div className="flex flex-col items-center justify-center h-full">

              <label className="cursor-pointer">

                <div className="w-32 h-32 rounded-full border-2 border-dashed border-gray-400 flex items-center justify-center text-4xl text-gray-500">
                  +
                </div>

                <input
                  type="file"
                  hidden
                  onChange={(e) =>
                    setImage(
                      URL.createObjectURL(
                        e.target.files[0]
                      )
                    )
                  }
                />
              </label>

              <p className="text-center mt-4 font-medium">
                Upload Profile Image
              </p>

              {image && (
                <img
                  src={image}
                  alt=""
                  className="w-24 h-24 rounded-full mt-4 object-cover"
                />
              )}
            </div>
          </div>
        </div>

        {/* Contact Section */}
        <div className="bg-white rounded-xl shadow p-5">
          <h2 className="font-semibold text-lg mb-5">
            Contact & Address Details
          </h2>

          <div className="grid md:grid-cols-2 xl:grid-cols-4 gap-4">

            <Input label="Phone Number" />
            <Input label="Alternate Phone Number" />

            <Input label="Emergency Contact Name" />
            <Input label="Emergency Contact Number" />

            <Input
              label="Address"
              className="xl:col-span-2"
            />

            <Input label="City" />
            <Input label="State" />

            <Input label="Country" />
            <Input label="Postal Code" />
          </div>
        </div>

        {/* Employment */}
        <div className="grid lg:grid-cols-2 gap-5">

          <div className="bg-white rounded-xl shadow p-5">
            <h2 className="font-semibold text-lg mb-5">
              Employment & Role Settings
            </h2>

            <div className="grid md:grid-cols-2 gap-4">

              <Input
                label="Joining Date"
                type="date"
              />

              <Input label="Basic Salary" />

              <Select
                label="Employee Status"
                options={[
                  "ACTIVE",
                  "INACTIVE",
                  "PROBATION",
                  "SUSPENDED",
                  "RESIGNED",
                  "TERMINATED",
                ]}
              />

              <Select
                label="Employment Type"
                options={[
                  "FULL_TIME",
                  "PART_TIME",
                  "CONTRACT",
                  "TEMPORARY",
                  "INTERN",
                ]}
              />

              <Select
                label="Department Or Role"
                options={[
                  "CASHIER",
                  "WAITER",
                  "HOST",
                  "CHEF",
                  "ACCOUNTANT",
                  "DELIVERY_RIDER",
                ]}
              />
            </div>
          </div>

          {/* Description */}
          <div className="bg-white rounded-xl shadow p-5">
            <h2 className="font-semibold text-lg mb-5">
              Additional Details & Description
            </h2>

            <label className="block mb-2 text-sm font-medium">
              Description
            </label>

            <textarea
              rows={8}
              className="w-full border rounded-lg p-3 focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>
        </div>

        {/* Buttons */}
        <div className="flex justify-end gap-3">

          <button
            type="button"
            className="px-6 py-2 border rounded-lg"
          >
            Cancel
          </button>

          <button
            type="submit"
            className="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
          >
            Save Employee
          </button>

        </div>

      </form>
    </div>
    </div>
  );
}

function Input({
  label,
  type = "text",
}) {
  return (
    <div>
      <label className="block text-sm font-medium mb-1">
        {label}
      </label>

      <input
        type={type}
        className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500 outline-none"
      />
    </div>
  );
}

function Select({
  label,
  options,
}) {
  return (
    <div>
      <label className="block text-sm font-medium mb-1">
        {label}
      </label>

      <select className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500 outline-none">
        <option>Select</option>

        {options.map((item) => (
          <option key={item}>
            {item}
          </option>
        ))}
      </select>
    </div>
  );
}