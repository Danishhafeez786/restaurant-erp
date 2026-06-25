import Sidebar from "../../components/Sidebar";
import SubscriptionPlanTable from "./SubscriptionPlanTable";

export default function SubscriptionPlans() {
  return (
    <div className="min-h-screen bg-gray-100 lg:flex">
      <Sidebar />

      <div className="flex-1 p-4 md:p-6 overflow-y-auto">
        <SubscriptionPlanTable />
      </div>
    </div>
  );
}