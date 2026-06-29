import Sidebar from "../../components/Sidebar";
import RoleTable from "./RoleTable";

export default function Organizations() {
  return (
    <div className="min-h-screen bg-gray-100 lg:flex">
      <Sidebar />

      <div className="flex-1 p-4 md:p-6 overflow-y-auto">
        <RoleTable />
      </div>
    </div>
  );
}
