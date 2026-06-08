import {
  HomeIcon,
  ShoppingCartIcon,
  ClipboardDocumentListIcon,
  Squares2X2Icon,
  UserGroupIcon,
  TruckIcon,
  CubeIcon,
  ChartBarIcon,
  TableCellsIcon,
  ArrowRightOnRectangleIcon,
} from "@heroicons/react/24/outline";

import { useNavigate } from "react-router-dom";

const Dashboard = () => {

  const navigate = useNavigate();

  const user = JSON.parse(
    localStorage.getItem("user")
  );

  const logout = () => {

    localStorage.removeItem("accessToken");

    localStorage.removeItem("refreshToken");

    localStorage.removeItem("user");

    navigate("/login");

  };

  return (
    <div className="min-h-screen bg-gray-100 flex">

      {/* SIDEBAR */}
      <div className="w-[280px] bg-[#0d4039] text-white flex flex-col shadow-2xl">

        {/* LOGO */}
        <div className="px-8 py-7 border-b border-white/10">

          <h1 className="text-3xl font-extrabold">
            Foodie
            <span className="text-yellow-400">
              POS
            </span>
          </h1>

          <p className="text-sm text-gray-300 mt-2">
            Restaurant ERP System
          </p>

        </div>

        {/* USER INFO */}
        <div className="px-8 py-6 border-b border-white/10">

          <h2 className="font-semibold text-lg">
            {user?.firstName}
            {" "}
            {user?.lastName}
          </h2>

          <p className="text-sm text-gray-300 mt-1">
            {user?.email}
          </p>

          <div className="mt-3 inline-block bg-white/10 px-4 py-2 rounded-xl text-sm">
            {user?.role}
          </div>

        </div>

        {/* MENU */}
        <div className="flex-1 overflow-y-auto py-5">

          <nav className="space-y-2 px-4">

            <SidebarItem
              icon={<HomeIcon className="w-6 h-6" />}
              title="Dashboard"
            />

            <SidebarItem
              icon={<ShoppingCartIcon className="w-6 h-6" />}
              title="POS Orders"
            />

            <SidebarItem
              icon={<ClipboardDocumentListIcon className="w-6 h-6" />}
              title="Kitchen Orders"
            />

            <SidebarItem
              icon={<TableCellsIcon className="w-6 h-6" />}
              title="Table Reservation"
            />

            <SidebarItem
              icon={<Squares2X2Icon className="w-6 h-6" />}
              title="Menu Management"
            />

            <SidebarItem
              icon={<CubeIcon className="w-6 h-6" />}
              title="Inventory"
            />

            <SidebarItem
              icon={<TruckIcon className="w-6 h-6" />}
              title="Delivery Orders"
            />

            <SidebarItem
              icon={<UserGroupIcon className="w-6 h-6" />}
              title="Customers"
            />

            <SidebarItem
              icon={<ChartBarIcon className="w-6 h-6" />}
              title="Reports & Analytics"
            />

          </nav>

        </div>

        {/* LOGOUT */}
        <div className="p-5 border-t border-white/10">

          <button
            onClick={logout}
            className="w-full bg-red-500 hover:bg-red-600 transition-all duration-300 rounded-2xl py-4 font-semibold flex items-center justify-center gap-3"
          >

            <ArrowRightOnRectangleIcon className="w-6 h-6" />

            Logout

          </button>

        </div>

      </div>

      {/* MAIN CONTENT */}
      <div className="flex-1 overflow-y-auto">

        {/* HEADER */}
        <div className="bg-white px-8 py-6 shadow-sm flex items-center justify-between">

          <div>

            <h1 className="text-3xl font-bold text-gray-800">
              Dashboard
            </h1>

            <p className="text-gray-500 mt-1">
              Welcome back to your restaurant management system
            </p>

          </div>

          <div className="flex items-center gap-4">

            <div className="bg-green-100 text-green-700 px-5 py-2 rounded-xl text-sm font-semibold">
              Restaurant Online
            </div>

          </div>

        </div>

        {/* CONTENT */}
        <div className="p-8">

          {/* STATS */}
          <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-6">

            <StatCard
              title="Today's Orders"
              value="145"
            />

            <StatCard
              title="Today's Sales"
              value="Rs. 125,000"
            />

            <StatCard
              title="Reserved Tables"
              value="18"
            />

            <StatCard
              title="Delivery Orders"
              value="42"
            />

          </div>

          {/* SECOND ROW */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mt-8">

            {/* RECENT ORDERS */}
            <div className="lg:col-span-2 bg-white rounded-3xl shadow-md p-6">

              <div className="flex items-center justify-between mb-6">

                <h2 className="text-2xl font-bold text-gray-800">
                  Recent Orders
                </h2>

                <button className="text-[#0d4039] font-semibold">
                  View All
                </button>

              </div>

              <div className="space-y-4">

                <OrderRow
                  order="#ORD-1001"
                  customer="Ali Khan"
                  amount="Rs. 4,500"
                  status="Completed"
                />

                <OrderRow
                  order="#ORD-1002"
                  customer="Ahmed Raza"
                  amount="Rs. 2,800"
                  status="Pending"
                />

                <OrderRow
                  order="#ORD-1003"
                  customer="Danish Hafeez"
                  amount="Rs. 7,200"
                  status="Cooking"
                />

                <OrderRow
                  order="#ORD-1004"
                  customer="Usman"
                  amount="Rs. 1,950"
                  status="Delivered"
                />

              </div>

            </div>

            {/* QUICK ACTIONS */}
            <div className="bg-white rounded-3xl shadow-md p-6">

              <h2 className="text-2xl font-bold text-gray-800 mb-6">
                Quick Actions
              </h2>

              <div className="space-y-4">

                <QuickButton title="Create New Order" />

                <QuickButton title="Add Menu Item" />

                <QuickButton title="Reserve Table" />

                <QuickButton title="Add Customer" />

                <QuickButton title="Generate Report" />

              </div>

            </div>

          </div>

        </div>

      </div>

    </div>
  );
};

const SidebarItem = ({
  icon,
  title,
}) => {

  return (
    <button className="w-full flex items-center gap-4 px-5 py-4 rounded-2xl hover:bg-white/10 transition-all duration-300 text-left">

      {icon}

      <span className="font-medium">
        {title}
      </span>

    </button>
  );
};

const StatCard = ({
  title,
  value,
}) => {

  return (
    <div className="bg-white rounded-3xl shadow-md p-6">

      <h2 className="text-gray-500 text-sm">
        {title}
      </h2>

      <p className="text-4xl font-bold text-gray-800 mt-4">
        {value}
      </p>

    </div>
  );
};

const OrderRow = ({
  order,
  customer,
  amount,
  status,
}) => {

  return (
    <div className="flex items-center justify-between border border-gray-100 rounded-2xl p-4">

      <div>

        <h3 className="font-bold text-gray-800">
          {order}
        </h3>

        <p className="text-sm text-gray-500 mt-1">
          {customer}
        </p>

      </div>

      <div className="text-right">

        <p className="font-bold text-gray-800">
          {amount}
        </p>

        <span className="text-sm text-green-600 font-medium">
          {status}
        </span>

      </div>

    </div>
  );
};

const QuickButton = ({
  title,
}) => {

  return (
    <button className="w-full bg-[#0d4039] hover:bg-[#0a2d28] transition-all duration-300 text-white py-4 rounded-2xl font-semibold">

      {title}

    </button>
  );
};

export default Dashboard;