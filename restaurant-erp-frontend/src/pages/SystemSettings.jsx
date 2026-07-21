import { NavLink, Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";

const menus = [
  { title: "Subscription Plans", path: "/system-settings/subscription-plans" },
  { title: "Organizations", path: "/system-settings/organizations" },
  { title: "Branches", path: "/system-settings/branch" },
  { title: "Roles", path: "/system-settings/role" },
  { title: "Permissions", path: "/system-settings/permission" },
  { title: "Settings", path: "/system-settings/settings" },
];

export default function SystemSettings() {
  return (
    <div className="min-h-screen bg-gray-100 lg:flex">
      <Sidebar />

      <div className="flex-1 flex flex-col">

        {/* Page Header */}
        <div className="bg-white shadow-sm border-b px-6 py-5">
          <h1 className="text-2xl font-bold text-gray-800">
            System Settings
          </h1>

          <p className="text-gray-500 mt-1">
            Manage system configuration.
          </p>
        </div>

        {/* Top Navigation */}
        <div className="bg-white border-b shadow-sm">

          <div className="overflow-x-auto">

            <div className="flex whitespace-nowrap">

              {menus.map((menu) => (
                <NavLink
                  key={menu.path}
                  to={menu.path}
                  className={({ isActive }) =>
                    `px-6 py-4 text-sm font-semibold border-b-2 transition-all duration-200
                    ${
                      isActive
                        ? "border-green-600 text-green-700"
                        : "border-transparent text-gray-600 hover:text-green-600 hover:border-green-300"
                    }`
                  }
                >
                  {menu.title}
                </NavLink>
              ))}

            </div>

          </div>

        </div>

        {/* Content */}

        <div className="flex-1 p-6 overflow-y-auto">
          <Outlet />
        </div>

      </div>
    </div>
  );
}