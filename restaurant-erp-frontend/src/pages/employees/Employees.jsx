import Sidebar from "../../components/Sidebar";
import EmployeeTable from "../../components/EmployeeTable";

export default function Employees() {
  return (
    <div className="min-h-screen bg-gray-100 lg:flex">

      <Sidebar />

      <div className="flex-1 p-4 md:p-6 overflow-y-auto">
        <EmployeeTable />
      </div>

    </div>
  );
}