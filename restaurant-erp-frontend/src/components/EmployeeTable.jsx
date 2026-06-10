import { useState } from "react";

export default function EmployeeTable() {
  const [employees] = useState([
    {
      id: 1,
      name: "Ali Khan",
      role: "Chef",
      phone: "03001234567",
      status: "ACTIVE",
      salary: "50000",
    },
    {
      id: 2,
      name: "Ahmed Raza",
      role: "Cashier",
      phone: "03111234567",
      status: "PROBATION",
      salary: "35000",
    },
    {
      id: 3,
      name: "Usman Tariq",
      role: "Waiter",
      phone: "03211234567",
      status: "ACTIVE",
      salary: "30000",
    },
  ]);

  const getStatusColor = (status) => {
    switch (status) {
      case "ACTIVE":
        return "bg-green-100 text-green-700";
      case "PROBATION":
        return "bg-yellow-100 text-yellow-700";
      case "SUSPENDED":
        return "bg-red-100 text-red-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-md p-4 md:p-6">

      {/* HEADER */}
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-2xl font-bold text-gray-800">
          Employees Record
        </h2>

        <button className="bg-[#0d4039] text-white px-4 py-2 rounded-xl text-sm">
          + Add Employee
        </button>
      </div>

      {/* ================= DESKTOP TABLE ================= */}
      <div className="hidden md:block overflow-x-auto">
        <table className="w-full text-left min-w-[700px]">

          <thead>
            <tr className="border-b text-gray-500">
              <th className="py-3">Name</th>
              <th>Role</th>
              <th>Phone</th>
              <th>Salary</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {employees.map((emp) => (
              <tr
                key={emp.id}
                className="border-b hover:bg-gray-50 transition"
              >
                <td className="py-3 font-semibold">{emp.name}</td>
                <td>{emp.role}</td>
                <td>{emp.phone}</td>
                <td>Rs. {emp.salary}</td>

                <td>
                  <span
                    className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(
                      emp.status
                    )}`}
                  >
                    {emp.status}
                  </span>
                </td>

                <td>
                  <button className="text-blue-600 mr-3">Edit</button>
                  <button className="text-red-600">Delete</button>
                </td>
              </tr>
            ))}
          </tbody>

        </table>
      </div>

      {/* ================= MOBILE CARDS ================= */}
      <div className="md:hidden space-y-4">

        {employees.map((emp) => (
          <div
            key={emp.id}
            className="border rounded-2xl p-4 shadow-sm"
          >
            <div className="flex justify-between items-center">
              <h3 className="font-bold text-lg">{emp.name}</h3>

              <span
                className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
                  emp.status
                )}`}
              >
                {emp.status}
              </span>
            </div>

            <div className="mt-3 text-sm text-gray-600 space-y-1">
              <p><b>Role:</b> {emp.role}</p>
              <p><b>Phone:</b> {emp.phone}</p>
              <p><b>Salary:</b> Rs. {emp.salary}</p>
            </div>

            <div className="flex gap-3 mt-4">
              <button className="flex-1 bg-blue-500 text-white py-2 rounded-xl">
                Edit
              </button>

              <button className="flex-1 bg-red-500 text-white py-2 rounded-xl">
                Delete
              </button>
            </div>
          </div>
        ))}

      </div>

    </div>
  );
}