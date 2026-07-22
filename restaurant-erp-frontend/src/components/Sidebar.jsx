import { useState, useEffect } from "react";
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
  Bars3Icon,
  XMarkIcon,
  ChevronDownIcon,
  BuildingOfficeIcon,
} from "@heroicons/react/24/outline";

const Sidebar = () => {
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = useState(false);
  const [collapsed, setCollapsed] = useState(() => {
    return JSON.parse(localStorage.getItem("sidebarCollapsed")) || false;
  });
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
    localStorage.setItem(
      "sidebarCollapsed",
      JSON.stringify(collapsed)
    );
  }, [collapsed]);

  const menuGroups = [
    {
      title: "Dashboard",
      path: "/dashboard",
      icon: <HomeIcon className="w-5 h-5" />,
    },

    {
      title: "System Settings",
      path: "/system-settings",
      icon: <Cog6ToothIcon className="w-5 h-5" />
    },

    {
      title: "Employee Management",
      icon: <UserGroupIcon className="w-5 h-5" />,
      children: [
        {
          title: "Create Employee",
          path: "/create-employee",
          icon: <UserPlusIcon className="w-5 h-5" />,
        },
        {
          title: "Employee List",
          path: "/employee",
          icon: <ClipboardDocumentListIcon className="w-5 h-5" />,
        },
      ],
    },

    {
      title: "Orders",
      icon: <ShoppingCartIcon className="w-5 h-5" />,
      children: [
        {
          title: "POS Orders",
          path: "/pos-orders",
          icon: <Squares2X2Icon className="w-5 h-5" />,
        },
        {
          title: "Kitchen Orders",
          path: "/kitchen-orders",
          icon: <ClipboardDocumentListIcon className="w-5 h-5" />,
        },
        {
          title: "Delivery Orders",
          path: "/delivery",
          icon: <TruckIcon className="w-5 h-5" />,
        },
      ],
    },

    {
      title: "Restaurant Management",
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
      title: "Restaurant",
      icon: <TableCellsIcon className="w-5 h-5" />,
      children: [
        {
          title: "Table Reservation",
          path: "/tables",
          icon: <Squares2X2Icon className="w-5 h-5" />,
        },
        {
          title: "Menu Management",
          path: "/menu",
          icon: <ClipboardDocumentListIcon className="w-5 h-5" />,
        },
      ],
    },

    {
      title: "Inventory",
      icon: <CubeIcon className="w-5 h-5" />,
      children: [
        {
          title: "Inventory",
          path: "/inventory",
          icon: <ClipboardDocumentListIcon className="w-5 h-5" />,
        },
      ],
    },

    {
      title: "Customers",
      icon: <UserGroupIcon className="w-5 h-5" />,
      path: "/customers",
    },

    {
      title: "Reports",
      icon: <ChartBarIcon className="w-5 h-5" />,
      children: [
        {
          title: "Reports & Analytics",
          path: "/reports",
          icon: <ChartBarIcon className="w-5 h-5" />,
        },
      ],
    },
  ];

  const SidebarContent = () => (
    <>
      {/* USER INFO */}
      {!collapsed && (
        <div className="px-6 py-8 border-b border-white/10">
          <h2 className="font-bold text-xl text-white">{user?.fullName}</h2>

          <p className="text-gray-200 mt-4 font-medium">{user?.role}</p>
        </div>
      )}

      {/* MENUS */}
      <div className="flex-1 overflow-y-auto py-4">
        {menuGroups.map((menu) => (
          <div key={menu.title} className="mb-2 px-2">
            {/* SIMPLE MENU */}
            {!menu.children ? (
              <button
                title={collapsed ? menu.title : ""}
                onClick={() => {
                  navigate(menu.path);
                  setIsOpen(false);
                }}
                className={`w-full flex items-center px-4 py-3 rounded-lg hover:bg-white/10 transition text-left ${collapsed ? "justify-center" : "gap-3"
                  }`}
              >
                {menu.icon}

                {!collapsed && (
                  <span className="font-semibold">
                    {menu.title}
                  </span>
                )}
              </button>
            ) : (
              <>
                {/* ACCORDION HEADER */}
                <button
                  onClick={() => toggleMenu(menu.title)}
                  className={`w-full flex items-center justify-between px-4 py-3 rounded-lg transition
                    ${openMenu === menu.title
                      ? "bg-white/10 border border-white/20"
                      : "hover:bg-white/10"
                    }`}
                >
                  <div
                    className={`flex items-center ${collapsed ? "justify-center w-full" : "gap-3"
                      }`}
                  >
                    {menu.icon}

                    {!collapsed && (
                      <span className="font-semibold">
                        {menu.title}
                      </span>
                    )}
                  </div>

                  {!collapsed && (
                    <ChevronDownIcon
                      className={`w-5 h-5 transition-transform duration-300 ${openMenu === menu.title ? "rotate-180" : ""
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

      {/* LOGOUT */}
      <div className="p-4 border-t border-white/10">
        <button
          onClick={logout}
          className="w-full bg-red-500 hover:bg-red-600 rounded-xl py-3 font-semibold flex items-center justify-center gap-2"
        >
          <ArrowRightOnRectangleIcon className="w-5 h-5" />
          {!collapsed && "Logout"}
        </button>
      </div>
    </>
  );

  return (
    <>
      {/* MOBILE HEADER */}
      <div className="lg:hidden bg-[#0d4039] text-white px-4 py-4 flex justify-between items-center shadow-lg">
        <h1 className="text-xl font-bold">
          Foodie
          <span className="text-yellow-400">POS</span>
        </h1>

        <button
          onClick={() => setIsOpen(!isOpen)}
          className="p-2 rounded-lg hover:bg-white/10"
        >
          <Bars3Icon className="w-6 h-6" />
        </button>
      </div>

      {/* MOBILE MENU */}
      {isOpen && (
        <div className="lg:hidden bg-[#0d4039] text-white min-h-screen">
          {/* USER INFO */}
          <div className="py-6 border-b border-white/10 text-center">
            <h2 className="font-bold text-xl">
              {user?.firstName} {user?.lastName}
            </h2>

            <p className="mt-3 text-gray-200">{user?.role}</p>
          </div>

          {/* MENUS */}
          <div className="py-4">
            {menuGroups.map((menu) => (
              <div key={menu.title}>
                {!menu.children ? (
                  <button
                    onClick={() => {
                      navigate(menu.path);
                      setIsOpen(false);
                    }}
                    className="w-full py-4 text-center font-semibold border-b border-white/10"
                  >
                    {menu.title}
                  </button>
                ) : (
                  <>
                    <button
                      onClick={() => toggleMenu(menu.title)}
                      className={`w-full py-4 px-6 flex justify-center items-center gap-3 font-semibold border-b border-white/10 ${openMenu === menu.title ? "bg-white/10" : ""
                        }`}
                    >
                      <span>{menu.title}</span>

                      <ChevronDownIcon
                        className={`w-5 h-5 transition-transform ${openMenu === menu.title ? "rotate-180" : ""
                          }`}
                      />
                    </button>

                    {openMenu === menu.title && (
                      <div className="flex flex-col items-center py-3 gap-4">
                        {menu.children.map((child) => (
                          <button
                            key={child.title}
                            onClick={() => {
                              navigate(child.path);
                              setIsOpen(false);
                            }}
                            className="text-gray-200 hover:text-yellow-400"
                          >
                            {child.title}
                          </button>
                        ))}
                      </div>
                    )}
                  </>
                )}
              </div>
            ))}
          </div>

          {/* LOGOUT */}
          <div className="mt-auto p-4">
            <button
              onClick={logout}
              className="w-full bg-red-500 hover:bg-red-600 py-4 rounded-lg font-semibold flex items-center justify-center gap-2"
            >
              <ArrowRightOnRectangleIcon className="w-5 h-5" />
              Logout
            </button>
          </div>
        </div>
      )}

      {/* DESKTOP SIDEBAR */}
      <div
        className={`hidden lg:flex h-screen bg-[#0d4039] text-white flex-col shadow-2xl transition-all duration-300 ${collapsed ? "w-20" : "w-[300px]"
          }`}
      >
        <div className="flex justify-between items-center">
          {!collapsed && (
            <div>
              <h1 className="text-3xl font-bold">
                Foodie
                <span className="text-yellow-400">POS</span>
              </h1>

              <p className="text-gray-300 mt-2 text-sm">
                Restaurant ERP System
              </p>
            </div>
          )}

          <button
            onClick={() => setCollapsed(!collapsed)}
            className="p-2 rounded-lg hover:bg-white/10"
          >
            <Bars3Icon className="w-6 h-6" />
          </button>
        </div>

        <SidebarContent />
      </div>
    </>
  );
};

export default Sidebar;
