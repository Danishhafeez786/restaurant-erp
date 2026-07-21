import Sidebar from "../../components/Sidebar";
import BranchTable from "./BranchTable";

export default function Branch() {
  return (
    <div className="flex-1 overflow-y-auto p-4 md:p-6">
      <BranchTable />
    </div>
  );
}