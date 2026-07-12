import Sidebar from "../../components/Sidebar";
import CategoryTable from "./CategoryTable";

export default function Employee() {
  return (
    <div className="min-h-screen bg-gray-100 lg:flex">
      <Sidebar />

      <div className="flex-1 p-4 md:p-6 overflow-y-auto">
        <CategoryTable />
      </div>
    </div>
  );
}