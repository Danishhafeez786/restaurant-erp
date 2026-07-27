import EmployeeTable from "./EmployeeTable";
import Sidebar from "../../components/Sidebar";
import { useState } from "react";

export default function Employee() {
  const [collapsed, setCollapsed] = useState(() => {
    const saved = localStorage.getItem("sidebarCollapsed");

    if (saved === null || saved === "undefined") {
      return true; // default collapsed
    }

    return JSON.parse(saved);
  });
  return (
    <div className="min-h-screen bg-gray-100">
      <Sidebar collapsed={collapsed} setCollapsed={setCollapsed} />

      <div
        className={`transition-all duration-300 ${
          collapsed ? "lg:ml-20" : "lg:ml-[300px]"
        }`}
      >
        <div className="flex-1 overflow-y-auto p-4 md:p-6">
          <EmployeeTable />
        </div>
      </div>
    </div>
  );
}
