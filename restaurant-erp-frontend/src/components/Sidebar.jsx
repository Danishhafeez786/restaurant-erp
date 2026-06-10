import { useState } from "react";
import { useNavigate } from "react-router-dom";


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
  Bars3Icon,
  XMarkIcon,
} from "@heroicons/react/24/outline";

const Sidebar = () => {
  const navigate = useNavigate();
  const [isOpen, setIsOpen] = useState(false);

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
      title: "Create Employee",
      icon: <HomeIcon className="w-6 h-6" />,
      path: "/create-employee",
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
  <>

    {/* MOBILE HEADER */}
    <div className="lg:hidden bg-[#0d4039] text-white px-4 py-4 flex justify-between items-center">
      <h1 className="text-xl font-bold">
        Foodie
        <span className="text-yellow-400">POS</span>
      </h1>

      <button onClick={() => setIsOpen(true)}>
        <Bars3Icon className="w-8 h-8" />
      </button>
    </div>

    {/* OVERLAY */}
    {isOpen && (
      <div
        className="fixed inset-0 bg-black/50 z-40 lg:hidden"
        onClick={() => setIsOpen(false)}
      />
    )}

    {/* MOBILE MENU */}
    <div
      className={`
        fixed top-0 left-0 h-full w-full
        bg-[#0d4039]
        z-50
        transform transition-transform duration-300
        lg:hidden
        flex flex-col
        ${isOpen ? "translate-x-0" : "-translate-x-full"}
      `}
    >

      {/* CLOSE BUTTON / HEADER */}
      <div className="flex justify-between items-center p-5 border-b border-white/10">
        <h1 className="text-2xl font-bold text-white">
          Foodie
          <span className="text-yellow-400">POS</span>
        </h1>

        <button onClick={() => setIsOpen(false)}>
          <XMarkIcon className="w-8 h-8 text-white" />
        </button>
      </div>

      {/* SCROLLABLE MENU AREA */}
      <div className="flex-1 overflow-y-auto">
        <nav className="flex flex-col items-center gap-8 text-white py-10">
          {menuItems.map((item) => (
            <button
              key={item.title}
              onClick={() => {
                navigate(item.path);
                setIsOpen(false);
              }}
              className="text-2xl font-medium hover:text-yellow-400"
            >
              {item.title}
            </button>
          ))}
        </nav>
      </div>
    </div>

    {/* DESKTOP SIDEBAR */}
    <div className="hidden lg:flex w-[280px] bg-[#0d4039] text-white flex-col shadow-2xl">

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

  </>
);
};

const SidebarItem = ({ icon, title, onClick }) => {
  return (
    <button
      onClick={onClick}
      className="w-full flex items-center gap-4 px-5 py-4 rounded-2xl hover:bg-white/10 transition"
    >
      {icon}
      <span>{title}</span>
    </button>
  );
};

export default Sidebar;