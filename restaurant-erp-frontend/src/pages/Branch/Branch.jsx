import Sidebar from "../../components/Sidebar";
import BranchTable from "./BranchTable";

export default function Branch() {
  return (
    <div className="min-h-screen bg-gray-100 lg:flex">
      <Sidebar />

      <div className="flex-1 overflow-y-auto p-4 md:p-6">
        <BranchTable />
      </div>
    </div>
  );
}