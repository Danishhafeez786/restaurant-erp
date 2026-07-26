import { useState } from "react";
import Sidebar from "../../components/Sidebar";
import EmployeeTable from "./EmployeeTable";

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
      <Sidebar
        collapsed={collapsed}
        setCollapsed={setCollapsed}
      />

      <div
        className={`transition-all duration-300 ${
          collapsed ? "lg:ml-20" : "lg:ml-[300px]"
        }`}
      >
        <div className="min-h-screen p-4 md:p-6 overflow-y-auto">
          <EmployeeTable />
        </div>
      </div>
    </div>
  );
}