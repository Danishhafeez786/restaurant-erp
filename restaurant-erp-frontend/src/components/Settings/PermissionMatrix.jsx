import { useMemo, useState } from "react";
import {
  Plus,
  Eye,
  Pencil,
  Trash2,
  RotateCcw,
  ChevronDown,
  Folder,
  Check,
  Shield,
  CheckCheck,
} from "lucide-react";

export default function PermissionMatrix({
  loading,
  roles = [],
  modules = [],
  assignments = [],
  onPermissionToggle,
}) {
  const [expandedModules, setExpandedModules] = useState([]);

  const ACTIONS = [
    {
      key: "CREATE",
      label: "Create",
      icon: Plus,
      color: "text-green-600",
      bg: "bg-green-50",
    },
    {
      key: "VIEW",
      label: "View",
      icon: Eye,
      color: "text-blue-600",
      bg: "bg-blue-50",
    },
    {
      key: "UPDATE",
      label: "Update",
      icon: Pencil,
      color: "text-amber-600",
      bg: "bg-amber-50",
    },
    {
      key: "DELETE",
      label: "Delete",
      icon: Trash2,
      color: "text-red-600",
      bg: "bg-red-50",
    },
    {
      key: "REACTIVATE",
      label: "Restore",
      icon: RotateCcw,
      color: "text-indigo-600",
      bg: "bg-indigo-50",
    },
  ];

  const gridStyle = {
  gridTemplateColumns: `420px repeat(${ACTIONS.length}, minmax(120px, 1fr))`,
};

  const toggleModule = (moduleKey) => {
    setExpandedModules((prev) =>
      prev.includes(moduleKey)
        ? prev.filter((item) => item !== moduleKey)
        : [...prev, moduleKey],
    );
  };

  const isExpanded = (moduleKey) => expandedModules.includes(moduleKey);

  const getAssignment = (roleId, permissionId) => {
    return assignments.find(
      (item) => item.roleId === roleId && item.permissionId === permissionId,
    );
  };

  /**
   * Converts
   * PERMISSION_CREATE
   * ROLE_CREATE
   * BRANCH_CREATE
   *
   * into
   * CREATE
   */
  const getAction = (permission) => {
    if (!permission?.code) return "";

    const parts = permission.code.split("_");

    return parts[parts.length - 1].toUpperCase();
  };

  /**
   * Find permission by action
   */
  const getPermissionByAction = (module, action) => {
    return module.permissions.find(
      (permission) => getAction(permission) === action,
    );
  };

  /**
   * Is role checked for every permission
   */
  const isAllSelected = (module, role) => {
    return module.permissions.every((permission) => {
      const assignment = getAssignment(role.id, permission.id);

      return assignment?.isActive;
    });
  };

  /**
   * Select All
   */
  const toggleAll = (module, role, checked) => {
    module.permissions.forEach((permission) => {
      onPermissionToggle({
        role,
        permission,
        assignment: getAssignment(role.id, permission.id),
        checked,
      });
    });
  };

  if (loading) {
    return (
      <div className="rounded-xl bg-white p-10 text-center">
        Loading permissions...
      </div>
    );
  }

  return (
    <div className="overflow-x-auto rounded-2xl border border-blue-100 bg-white shadow-sm">
      {modules.map((module) => {
        const moduleKey = module.id || module.module;

        return (
          <div
            key={moduleKey}
            className="mb-6 overflow-hidden rounded-2xl border border-blue-100 bg-white shadow-sm"
          >
            {/* Module Header */}
            <div
              onClick={() => toggleModule(moduleKey)}
              className="flex cursor-pointer items-center justify-between bg-gradient-to-r from-blue-50 to-indigo-50 px-6 py-5 transition-all hover:from-blue-100 hover:to-indigo-100"
            >
              <div className="flex items-center gap-4">
                <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-white shadow">
                  <Folder className="h-5 w-5 text-blue-600" />
                </div>

                <div>
                  <h3 className="text-lg font-bold text-gray-900 uppercase tracking-wide">
                    {module.module}
                  </h3>

                  <p className="mt-1 text-sm text-gray-500">
                    {module.permissions.length} permissions
                  </p>
                </div>
              </div>

              <div className="rounded-xl bg-white p-2 shadow-sm">
                <ChevronDown
                  className={`h-5 w-5 text-gray-600 transition-transform duration-300 ${
                    isExpanded(moduleKey) ? "rotate-180" : ""
                  }`}
                />
              </div>
            </div>

            {isExpanded(moduleKey) && (
              <>
                {/* Header */}
                <div
                  style={gridStyle}
                  className="grid items-center border-y bg-gray-50 px-6 py-4"
                >
                  <div className="flex items-center gap-3 font-bold text-gray-700">
                    <Shield className="h-5 w-5 text-blue-600" />
                    Roles
                  </div>

                  {ACTIONS.map((action) => {
                    const Icon = action.icon;

                    return (
                      <div
                        key={action.key}
                        className="flex flex-col items-center gap-1"
                      >
                        <div
                          className={`flex h-10 w-10 items-center justify-center rounded-xl ${action.bg}`}
                        >
                          <Icon className={`h-5 w-5 ${action.color}`} />
                        </div>

                        <span className="text-xs font-semibold text-gray-600">
                          {action.label}
                        </span>
                      </div>
                    );
                  })}
                </div>

                {/* Role Rows */}

                {roles.map((role) => (
                  <div
                    key={role.id}
                    style={gridStyle}
                    className=" grid items-center border-b border-gray-100 px-6 py-5 transition-all duration-200 hover:bg-blue-50/30"
                  >
                    {/* Role Name */}
                    <div className="flex items-center justify-between pr-6">
                      <div className="flex items-center gap-3">
                        <div className="flex h-11 w-11 items-center justify-center rounded-full bg-blue-100">
                          <Shield className="h-5 w-5 text-blue-600" />
                        </div>

                        <div>
                          <h4 className="font-semibold text-gray-900">
                            {role.roleName}
                          </h4>

                          <p className="text-xs text-gray-500">
                            Role Permissions
                          </p>
                        </div>
                      </div>

                      <button
                        onClick={() =>
                          toggleAll(module, role, !isAllSelected(module, role))
                        }
                        className={`
      flex
      items-center
      gap-2
      rounded-xl
      px-3
      py-2
      text-sm
      font-medium
      transition

      ${
        isAllSelected(module, role)
          ? "bg-green-100 text-green-700"
          : "bg-blue-50 text-blue-700 hover:bg-blue-100"
      }
    `}
                      >
                        <CheckCheck className="h-4 w-4" />

                        {isAllSelected(module, role)
                          ? "Selected"
                          : "Select All"}
                      </button>
                    </div>

                    {/* Permission Columns */}

                    {ACTIONS.map((action) => {
                      const permission = getPermissionByAction(
                        module,
                        action.key,
                      );

                      if (!permission) {
                        return (
                          <div key={action.key} className="flex justify-center">
                            <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-gray-100 text-gray-300">
                              —
                            </div>
                          </div>
                        );
                      }

                      const assignment = getAssignment(role.id, permission.id);

                      return (
                        <div key={action.key} className="flex justify-center">
                          <button
                            onClick={() =>
                              onPermissionToggle({
                                role,
                                permission,
                                assignment,
                                checked: !assignment?.isActive,
                              })
                            }
                            className={`flex h-6 w-6 items-center justify-center rounded-md border shadow-sm transition-all duration-200 ${
                              assignment?.isActive
                                ? "border-blue-600 bg-blue-600 text-white shadow-md"
                                : "border-gray-200 bg-white hover:border-blue-500 hover:bg-blue-50"
                            }`}
                          >
                            {assignment?.isActive && (
                              <Check className="h-5 w-5" />
                            )}
                          </button>
                        </div>
                      );
                    })}
                  </div>
                ))}
              </>
            )}
          </div>
        );
      })}
    </div>
  );
}
