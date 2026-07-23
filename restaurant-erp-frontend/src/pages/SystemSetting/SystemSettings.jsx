import { NavLink, Outlet } from "react-router-dom";
import Sidebar from "../../components/Sidebar";

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

      <div className="flex flex-1 flex-col">
        {/* Header */}
        <div className="border-b bg-white shadow-sm">
          <div className="px-4 py-4 sm:px-6 lg:px-8 lg:py-5">
            {/* Top Section */}
            <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
              {/* Left */}
              <div>
                <h1 className="text-xl font-bold text-gray-800 sm:text-2xl">
                  System Settings
                </h1>

                <p className="mt-1 text-sm text-gray-500">
                  Manage system configuration.
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
                    const active =
                      location.pathname === "/system-settings" && index === 0;

                    return (
                      <NavLink
                        key={menu.path}
                        to={menu.path}
                        className={({ isActive }) =>
                          `px-4 py-2 rounded-sm text-sm font-semibold transition-all
                        ${isActive || active
                            ? "bg-green-600 text-white shadow-md"
                            : "text-gray-600 hover:bg-white hover:text-green-600 hover:shadow"
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
