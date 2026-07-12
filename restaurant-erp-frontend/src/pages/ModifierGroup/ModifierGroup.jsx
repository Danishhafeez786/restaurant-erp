import Sidebar from "../../components/Sidebar";
import ModifierGroupTable from "./ModifierGroupTable";

export default function SubscriptionPlans() {
  return (
    <div className="min-h-screen bg-gray-100 lg:flex">
      <Sidebar />

      <div className="flex-1 p-4 md:p-6 overflow-y-auto">
        <ModifierGroupTable />
      </div>
    </div>
  );
}