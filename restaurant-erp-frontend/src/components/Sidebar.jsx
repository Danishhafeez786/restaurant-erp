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

const Sidebar = () => {
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem("user"));

  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("user");

    navigate("/login");
  };

  const menuItems = [
    {
      title: "Dashboard",
      icon: <HomeIcon className="w-6 h-6" />,
      path: "/dashboard",
    },
    {
      title: "POS Orders",
      icon: <ShoppingCartIcon className="w-6 h-6" />,
      path: "/pos-orders",
    },
    {
      title: "Kitchen Orders",
      icon: <ClipboardDocumentListIcon className="w-6 h-6" />,
      path: "/kitchen-orders",
    },
    {
      title: "Table Reservation",
      icon: <TableCellsIcon className="w-6 h-6" />,
      path: "/tables",
    },
    {
      title: "Menu Management",
      icon: <Squares2X2Icon className="w-6 h-6" />,
      path: "/menu",
    },
    {
      title: "Inventory",
      icon: <CubeIcon className="w-6 h-6" />,
      path: "/inventory",
    },
    {
      title: "Delivery Orders",
      icon: <TruckIcon className="w-6 h-6" />,
      path: "/delivery",
    },
    {
      title: "Customers",
      icon: <UserGroupIcon className="w-6 h-6" />,
      path: "/customers",
    },
    {
      title: "Reports & Analytics",
      icon: <ChartBarIcon className="w-6 h-6" />,
      path: "/reports",
    },
  ];

  return (
    <div className="w-[280px] bg-[#0d4039] text-white flex flex-col shadow-2xl">
      {/* LOGO */}
      <div className="px-8 py-7 border-b border-white/10">
        <h1 className="text-3xl font-extrabold">
          Foodie
          <span className="text-yellow-400">POS</span>
        </h1>

        <p className="text-sm text-gray-300 mt-2">
          Restaurant ERP System
        </p>
      </div>

      {/* USER INFO */}
      <div className="px-8 py-6 border-b border-white/10">
        <h2 className="font-semibold text-lg">
          {user?.firstName} {user?.lastName}
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
          {menuItems.map((item) => (
            <SidebarItem
              key={item.title}
              icon={item.icon}
              title={item.title}
              onClick={() => navigate(item.path)}
            />
          ))}
        </nav>
      </div>

      {/* LOGOUT */}
      <div className="p-5 border-t border-white/10">
        <button
          onClick={logout}
          className="w-full bg-red-500 hover:bg-red-600 rounded-2xl py-4 font-semibold flex items-center justify-center gap-3"
        >
          <ArrowRightOnRectangleIcon className="w-6 h-6" />
          Logout
        </button>
      </div>
    </div>
  );
};

const SidebarItem = ({ icon, title, onClick }) => {
  return (
    <button
      onClick={onClick}
      className="w-full flex items-center gap-4 px-5 py-4 rounded-2xl hover:bg-white/10 text-left"
    >
      {icon}
      <span>{title}</span>
    </button>
  );
};

export default Sidebar;