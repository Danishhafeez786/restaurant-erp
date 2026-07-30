import { useState } from "react";
import { NavLink, Outlet } from "react-router-dom";
import Sidebar from "../../components/Sidebar";

const menus = [
  { title: "Categories", path: "/restaurant-settings/category-management" },
  { title: "Modifier Groups", path: "/restaurant-settings/modifier-group" },
  { title: "Modifiers", path: "/restaurant-settings/modifier" },
  { title: "Tables", path: "/restaurant-settings/table-management" },
];

export default function RestaurantSettings() {
  const [collapsed, setCollapsed] = useState(() => {
    const saved = localStorage.getItem("sidebarCollapsed");

    if (saved === null || saved === "undefined") {
      return true;
    }

    return JSON.parse(saved);
  });

  return (
    <div className="min-h-screen bg-gray-100 lg:flex">
      <Sidebar collapsed={collapsed} setCollapsed={setCollapsed} />

      <div
        className={`flex flex-1 flex-col transition-all duration-300
      ${collapsed ? "lg:ml-20" : "lg:ml-[300px]"}`}
      >
        {/* Header */}
        <div className="border-b bg-white shadow-sm">
          <div className="px-4 py-4 sm:px-6 lg:px-8 lg:py-5">
            {/* Top Section */}
            <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
              {/* Left */}
              <div>
                <h1 className="text-xl font-bold text-gray-800 sm:text-2xl">
                  Restaurant Settings
                </h1>

                <p className="mt-1 text-sm text-gray-500">
                  Manage restaurant configuration.
                </p>
              </div>

              {/* Navigation */}
              <div className="w-full lg:flex-1 lg:flex lg:justify-center">
                <div
                  className="
                flex
                gap-2
                rounded-xl
                bg-gray-100
                p-1
                overflow-x-auto
                scrollbar-hide
                whitespace-nowrap
              "
                >
                  {menus.map((menu, index) => {
                    return (
                      <NavLink
                        key={menu.path}
                        to={menu.path}
                        end
                        className={({ isActive }) =>
                          `px-4 py-2 rounded-lg text-sm font-semibold transition-all duration-200 ${
                            isActive
                              ? "bg-blue-600 hover:bg-blue-700 text-white shadow-lg"
                              : "text-gray-600 hover:bg-gray-200 hover:text-[#375eb2]"
                          }`
                        }
                      >
                        {menu.title}
                      </NavLink>
                    );
                  })}
                </div>
              </div>

              {/* Desktop Spacer */}
              <div className="hidden lg:block w-56" />
            </div>
          </div>
        </div>

        {/* Page Content */}
        <div className="flex-1 overflow-y-auto p-4 sm:p-6">
          <Outlet />
        </div>
      </div>
    </div>
  );
}
