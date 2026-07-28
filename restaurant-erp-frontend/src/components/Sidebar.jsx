import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";

import {
  HomeIcon,
  UserGroupIcon,
  UserPlusIcon,
  ShoppingCartIcon,
  Cog6ToothIcon,
  ClipboardDocumentListIcon,
  TableCellsIcon,
  Squares2X2Icon,
  CubeIcon,
  TruckIcon,
  ChartBarIcon,
  ArrowRightOnRectangleIcon,
  XMarkIcon,
  ChevronDownIcon,
  BuildingOfficeIcon,
  ChevronUpIcon,
  UserCircleIcon,
  UserIcon,
  ChevronRightIcon,
} from "@heroicons/react/24/outline";

import { UserRound, LogOut, LifeBuoy, PanelLeft } from "lucide-react";

const Sidebar = ({ collapsed, setCollapsed }) => {
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = useState(false);
  const userMenuRef = useRef(null);
  const [mobileUserMenuOpen, setMobileUserMenuOpen] = useState(false);
  const [desktopUserMenuOpen, setDesktopUserMenuOpen] = useState(false);
  const [userMenuOpen, setUserMenuOpen] = useState(false);
  const [openMenu, setOpenMenu] = useState(null);

  const user = JSON.parse(localStorage.getItem("user"));
  const toggleMenu = (menu) => {
    setOpenMenu(openMenu === menu ? null : menu);
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("user");

    navigate("/login");
  };

  useEffect(() => {
    localStorage.setItem("sidebarCollapsed", JSON.stringify(collapsed));
  }, [collapsed]);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (userMenuRef.current && !userMenuRef.current.contains(event.target)) {
        setDesktopUserMenuOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const menuGroups = [
    {
      title: "Dashboard",
      path: "/dashboard",
      icon: <HomeIcon className="w-5 h-5" />,
    },

    {
      title: "Employee",
      path: "/employee",
      icon: <ClipboardDocumentListIcon className="w-5 h-5" />,
    },

    {
      title: "Orders",
      path: "/pos-orders",
      icon: <Squares2X2Icon className="w-5 h-5" />,
    },

    {
      title: "Restaurant",
      icon: <TableCellsIcon className="w-5 h-5" />,
      children: [
        {
          title: "Category",
          path: "/category-management",
          icon: <Squares2X2Icon className="w-5 h-5" />,
        },
        {
          title: "Floor",
          path: "/floor-management",
          icon: <ClipboardDocumentListIcon className="w-5 h-5" />,
        },
        {
          title: "Table",
          path: "/table-management",
          icon: <Squares2X2Icon className="w-5 h-5" />,
        },
        {
          title: "Modifier Group",
          path: "/modifier-group",
          icon: <Squares2X2Icon className="w-5 h-5" />,
        },
        {
          title: "Modifier",
          path: "/modifier",
          icon: <Squares2X2Icon className="w-5 h-5" />,
        },
      ],
    },

    {
      title: "Inventory",
      path: "/inventory",
      icon: <ClipboardDocumentListIcon className="w-5 h-5" />,
    },

    {
      title: "Customers",
      icon: <UserGroupIcon className="w-5 h-5" />,
      path: "/customers",
    },

    {
      title: "Settings",
      path: "/system-settings",
      icon: <Cog6ToothIcon className="w-5 h-5" />,
    },

    {
      title: "Reports & Analytics",
      path: "/reports",
      icon: <ChartBarIcon className="w-5 h-5" />,
    },
  ];

  const SidebarContent = () => (
    <>
      {/* MENUS */}
      <div className="flex-1 overflow-y-auto py-4 ">
        {menuGroups.map((menu) => (
          <div key={menu.title} className="relative mb-2 px-2 group">
            {/* SIMPLE MENU */}
            {!menu.children ? (
              <button
                title={collapsed ? menu.title : ""}
                onClick={() => {
                  setIsOpen(false);

                  if (window.innerWidth >= 1024) {
                    setCollapsed(true);
                  }

                  navigate(menu.path);
                }}
                className={`w-full flex items-center px-4 py-3 rounded-lg hover:bg-white/10 transition text-left ${
                  collapsed ? "justify-center" : "gap-3"
                }`}
              >
                {menu.icon}

                {!collapsed && (
                  <span className="font-semibold">{menu.title}</span>
                )}
              </button>
            ) : (
              <>
                {/* ACCORDION HEADER */}
                <button
                  onClick={() => toggleMenu(menu.title)}
                  className={`w-full flex items-center justify-between px-4 py-3 rounded-lg transition
                    ${
                      openMenu === menu.title
                        ? "bg-white/10 border border-white/20"
                        : "hover:bg-white/10"
                    }`}
                >
                  <div
                    className={`flex items-center ${
                      collapsed ? "justify-center w-full" : "gap-3"
                    }`}
                  >
                    {menu.icon}

                    {!collapsed && (
                      <span className="font-semibold">{menu.title}</span>
                    )}
                  </div>

                  {!collapsed && (
                    <ChevronDownIcon
                      className={`w-5 h-5 transition-transform duration-300 ${
                        openMenu === menu.title ? "rotate-180" : ""
                      }`}
                    />
                  )}
                </button>

                {/* SUBMENU */}
                {!collapsed && openMenu === menu.title && (
                  <div className="ml-8 mt-3 flex flex-col gap-3">
                    {menu.children.map((child) => (
                      <button
                        key={child.title}
                        onClick={() => {
                          navigate(child.path);
                          setIsOpen(false);
                        }}
                        className="text-left text-gray-200 hover:text-yellow-400 transition"
                      >
                        <div className="flex items-center gap-3">
                          {child.icon}

                          <span className="font-semibold">{child.title}</span>
                        </div>
                      </button>
                    ))}
                  </div>
                )}
              </>
            )}
          </div>
        ))}
      </div>
    </>
  );

  return (
    <>
      {/* MOBILE HEADER */}
      <div
        ref={userMenuRef}
        className="lg:hidden bg-[#111827] text-white h-16 px-4 flex items-center justify-between shadow-lg relative"
      >
        {/* LEFT SIDE */}
        <div className="flex items-center gap-3">
          {/* Hamburger */}
          <button
            onClick={() => setIsOpen(!isOpen)}
            className="p-2 rounded-lg hover:bg-white/10 transition"
          >
            <PanelLeft className="w-6 h-6" />
          </button>

          {/* Brand */}
          <h1 className="text-xl font-bold">
            Foodie
            <span className="text-yellow-400">POS</span>
          </h1>
        </div>

        {/* RIGHT SIDE USER */}
        <button
          onClick={() => setMobileUserMenuOpen(!mobileUserMenuOpen)}
          className="
      w-10 h-10 
      rounded-full 
      bg-blue-500
      flex
      items-center
      justify-center
      hover:ring-2
      hover:ring-white
    "
        >
          <UserRound className="w-6 h-6" />
        </button>
      </div>

      {/* User Dropdown */}
      {mobileUserMenuOpen && (
        <div className="lg:hidden absolute right-4 top-16 w-64 text-white rounded-2xl bg-slate-800 border border-slate-700 shadow-2xl overflow-hidden z-50">
          {/* User Info */}
          <div className="p-4 border-b border-slate-700 flex items-center gap-3">
            <div>
              <p className="font-semibold">{user?.fullName || "John Doe"}</p>

              <p className="text-sm text-gray-400">
                {user?.email || "john@email.com"}
              </p>
            </div>
          </div>

          {/* Menu Items */}
          <button className="w-full px-4 py-3 flex items-center gap-3 hover:bg-slate-700 transition">
            <UserCircleIcon className="w-5 h-5" />
            Profile
          </button>

          <button className="w-full px-4 py-3 flex items-center gap-3 hover:bg-slate-700 transition">
            <Cog6ToothIcon className="w-5 h-5" />
            Settings
          </button>

          <button
            onClick={logout}
            className="w-full px-4 py-3 flex items-center gap-3 text-red-400 hover:bg-red-500 hover:text-white transition"
          >
            <ArrowRightOnRectangleIcon className="w-5 h-5" />
            Logout
          </button>
        </div>
      )}

      {/* MOBILE MENU */}
      <div
        className={`
    lg:hidden
    fixed
    top-0
    left-0
    h-screen
    w-[75%]
    max-w-sm
    bg-[#111827]
    text-white
    z-50
    shadow-2xl
    transform
    transition-transform
    duration-300
    flex
    flex-col
    ${isOpen ? "translate-x-0" : "-translate-x-full"}
  `}
      >
        {/* USER INFO */}
        <div className="py-6 border-b border-white/10 text-left px-6">
          <h2 className="font-bold text-xl">
            {user?.firstName} {user?.lastName}
          </h2>

          <p className="mt-3 text-gray-200">{user?.role}</p>
        </div>

        {/* MENUS */}
        <div className="flex-1 overflow-y-auto py-4">
          {menuGroups.map((menu) => (
            <div key={menu.title}>
              {!menu.children ? (
                <button
                  onClick={() => {
                    navigate(menu.path);
                    setIsOpen(false);
                  }}
                  className="w-full px-6 py-4 flex items-center gap-3 font-semibold border-b border-white/10 hover:bg-white/10 transition-colors"
                >
                  {menu.icon}
                  <span>{menu.title}</span>
                </button>
              ) : (
                <>
                  <button
                    onClick={() => toggleMenu(menu.title)}
                    className={`w-full py-4 px-6 flex items-center justify-between font-semibold border-b border-white/10 transition-colors ${
                      openMenu === menu.title
                        ? "bg-white/10"
                        : "hover:bg-white/5"
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      {menu.icon}
                      <span>{menu.title}</span>
                    </div>

                    <ChevronDownIcon
                      className={`w-5 h-5 transition-transform duration-200 ${
                        openMenu === menu.title ? "rotate-180" : ""
                      }`}
                    />
                  </button>

                  {openMenu === menu.title && (
                    <div className="flex flex-col py-3 gap-3 ">
                      {menu.children.map((child) => (
                        <button
                          key={child.title}
                          onClick={() => {
                            navigate(child.path);
                            setIsOpen(false);
                          }}
                          className="w-full flex items-center gap-3 px-10 py-3 text-left text-gray-200 hover:bg-white/10 hover:text-white transition"
                        >
                          {child.icon}
                          <span>{child.title}</span>
                        </button>
                      ))}
                    </div>
                  )}
                </>
              )}
            </div>
          ))}
        </div>
      </div>

      {isOpen && (
        <div
          onClick={() => setIsOpen(false)}
          className="
      lg:hidden
      fixed
      inset-0
      bg-black/40
      z-40
    "
        />
      )}

      {/* DESKTOP SIDEBAR */}
      <div
        className={`hidden lg:flex fixed left-0 top-0 h-screen bg-[#111827]
    text-white flex-col shadow-2xl transition-all duration-300
    ${collapsed ? "w-20" : "w-[300px]"}`}
      >
        {/* Header */}
        <div className="p-6 border-b border-white/10">
          <div className="flex justify-between items-start">
            {!collapsed && (
              <div>
                <h1 className="text-3xl font-bold">
                  Foodie<span className="text-yellow-400">POS</span>
                </h1>

                <p className="text-gray-300 mt-2 text-sm">
                  Restaurant ERP System
                </p>
              </div>
            )}

            <button
              onClick={() => setCollapsed(!collapsed)}
              className="p-2 rounded-lg hover:bg-white/10 cursor-ew-resize"
            >
              <PanelLeft className="w-5 h-5 " />
            </button>
          </div>
        </div>

        {/* Navigation */}
        <div className="flex-1 overflow-y-auto sidebar-scroll">
          <SidebarContent />
        </div>

        {/* LOGOUT */}
        <div
          ref={userMenuRef}
          className="relative p-4 border-t border-white/10"
        >
          {/* User Card */}
          <button
            onClick={() => setDesktopUserMenuOpen(!desktopUserMenuOpen)}
            className={`w-full rounded-2xl hover:bg-slate-700 transition-all p-1 flex items-center ${
              collapsed ? "justify-center" : "justify-between"
            }`}
          >
            <div className="flex items-center gap-3">
              {/* Avatar */}
              <div className="w-11 h-11 rounded-full bg-blue-500 flex items-center justify-center text-white font-bold">
                <UserRound className="w-6 h-6" />
              </div>

              {!collapsed && (
                <div className="text-left">
                  <p className="font-semibold text-sm text-white">
                    {user?.fullName || "John Doe"}
                  </p>
                  <p className="text-gray-200 font-medium">{user?.role}</p>
                </div>
              )}
            </div>
          </button>

          {/* Dropdown */}
          {desktopUserMenuOpen && (
            <div
              className={`absolute z-50 rounded-xl bg-slate-800 shadow-xl border border-slate-700 overflow-hidden
      ${
        collapsed ? "left-full ml-3 bottom-4 w-52" : "bottom-24 left-4 right-4"
      }`}
            >
              <button className="w-full px-4 py-3 flex items-center gap-3 text-left hover:bg-slate-700 transition">
                <UserIcon className="w-5 h-5 text-gray-300" />
                <span>Profile</span>
              </button>

              <button className="w-full px-4 py-3 flex items-center gap-3 text-left hover:bg-slate-700 transition">
                <Cog6ToothIcon className="w-5 h-5 text-gray-300" />
                <span>Settings</span>
              </button>

              <button className="w-full px-4 py-3 flex items-center gap-3 text-left hover:bg-slate-700 transition border-t border-white/10">
                <LifeBuoy className="w-5 h-5 text-gray-300" />
                <span>Help</span>
              </button>

              <button
                onClick={logout}
                className="w-full px-4 py-3 flex items-center gap-3 text-left text-red-400 hover:bg-red-500 hover:text-white transition"
              >
                <LogOut className="w-5 h-5" />
                <span>Logout</span>
              </button>
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default Sidebar;
