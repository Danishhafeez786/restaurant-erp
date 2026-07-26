import { useState } from "react";
import {
  ChevronDown,
  Folder,
  Plus,
  Eye,
  Pencil,
  Trash2,
  RotateCcw,
  Check,
} from "lucide-react";

export default function PermissionMatrix({
  loading,
  roles = [],
  modules = [],
  assignments = [],
  onPermissionToggle,
}) {
  const [expandedModules, setExpandedModules] = useState([]);

  const gridStyle = {
    gridTemplateColumns: `260px repeat(${roles.length},120px)`,

    minWidth: `${260 + roles.length * 120}px`,
  };

  const toggleModule = (moduleKey) => {
    setExpandedModules((prev) => {
      if (prev.includes(moduleKey)) {
        return prev.filter((id) => id !== moduleKey);
      }

      return [...prev, moduleKey];
    });
  };

  const isExpanded = (moduleKey) => {
    return expandedModules.includes(moduleKey);
  };

  const getAssignment = (roleId, permissionId) => {
    return assignments.find(
      (item) => item.roleId === roleId && item.permissionId === permissionId,
    );
  };

  const isAllSelected = (module, role) => {
    return module.permissions.every((permission) => {
      const assignment = getAssignment(role.id, permission.id);

      return assignment?.isActive;
    });
  };

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
      <div
        className="
                bg-white
                rounded-xl
                p-10
                text-center
            "
      >
        Loading permissions...
      </div>
    );
  }

  return (
    <div
      className="
    bg-white
    rounded-2xl
    border
    border-blue-100
    shadow-sm
    overflow-x-auto
"
    >
      {/* HEADER */}

      <div
        style={gridStyle}
        className="
                    grid
                    items-center
                    bg-black-50
                    px-6
                    py-4
                    font-semibold
                    text-black-700
                "
      >
        <div>Permission</div>

        {roles.map((role) => (
          <div
            key={role.id}
            className="
                                text-center
                                truncate
                            "
          >
            {role.roleName}
          </div>
        ))}
      </div>

      {modules.map((module) => {
        const moduleKey = module.id || module.module;

        return (
          <div
            key={moduleKey}
            className="
                                border-t
                                border-black-100
                            "
          >
            {/* MODULE HEADER */}

            <div
              onClick={() => toggleModule(moduleKey)}
              className="
                                    flex
                                    items-center
                                    justify-between
                                    px-6
                                    py-4
                                    cursor-pointer
                                    hover:bg-black-50
                                    transition
                                "
            >
              <div
                className="
                                    flex
                                    items-center
                                    gap-4
                                "
              >
                <div
                  className="
                                        w-10
                                        h-10
                                        rounded-xl
                                        bg-black-100
                                        flex
                                        items-center
                                        justify-center
                                    "
                >
                  <Folder
                    className="
                                                w-5
                                                text-blue-600
                                            "
                  />
                </div>

                <div>
                  <h3
                    className="
                                            font-semibold
                                            text-gray-800
                                        "
                  >
                    {module.module}
                  </h3>

                  <p
                    className="
                                            text-xs
                                            text-black-500
                                        "
                  >
                    {module.permissions.length} permissions
                  </p>
                </div>
              </div>

              <ChevronDown
                className={`
                                        w-5
                                        h-5
                                        text-black-600
                                        transition-transform
                                        duration-300 

                                        ${
                                          isExpanded(moduleKey)
                                            ? "rotate-180"
                                            : ""
                                        }

                                    `}
              />
            </div>

            {isExpanded(moduleKey) && (
              <div>
                {/* SELECT ALL */}

                <div
                  style={gridStyle}
                  className="
                                                grid
                                                items-center
                                                bg-blue-50/50
                                                px-6
                                                py-3
                                                border-t
                                                border-blue-100
                                            "
                >
                  <div
                    className="
                                                pl-10
                                                font-medium
                                                text-blue-700
                                            "
                  >
                    Select All
                  </div>

                  {roles.map((role) => (
                    <div
                      key={role.id}
                      className="
                                                            flex
                                                            justify-center
                                                            items-center
                                                        "
                    >
                      <input
                        type="checkbox"
                        checked={isAllSelected(module, role)}
                        onChange={(e) =>
                          toggleAll(module, role, e.target.checked)
                        }
                        className="
        w-5
        h-5
        accent-blue-600
        cursor-pointer
    "
                      />
                    </div>
                  ))}
                </div>

                {module.permissions.map((permission) => (
                  <div
                    key={permission.id}
                    style={gridStyle}
                    className="
                                                        grid
                                                        items-center
                                                        px-6
                                                        py-3
                                                        border-t
                                                        border-blue-50
                                                        hover:bg-blue-50/40
                                                    "
                  >
                    <div
                      className="
                                                        flex
                                                        items-center
                                                        gap-3
                                                        pl-10
                                                    "
                    >
                      {permissionIcon(permission.code)}

                      <span>{permission.name}</span>
                    </div>

                    {roles.map((role) => {
                      const assignment = getAssignment(role.id, permission.id);

                      return (
                        <div
                          key={role.id}
                          className="
                                                                        flex
                                                                        justify-center
                                                                        items-center
                                                                    "
                        >
                          <button
                            onClick={() =>
                              onPermissionToggle({
                                role,

                                permission,

                                assignment,

                                checked: !assignment?.isActive,
                              })
                            }
                            className={`
                                                                            w-6
                                                                            h-6
                                                                            rounded-md
                                                                            border
                                                                            flex
                                                                            items-center
                                                                            justify-center
                                                                            transition

                                                                            ${
                                                                              assignment?.isActive
                                                                                ? "bg-blue-600 border-blue-600 text-white"
                                                                                : "border-blue-200 hover:border-blue-500"
                                                                            }

                                                                        `}
                          >
                            {assignment?.isActive && <Check className="w-4" />}
                          </button>
                        </div>
                      );
                    })}
                  </div>
                ))}
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
}

function permissionIcon(code) {
  const icons = {
    CREATE: <Plus className="w-4 text-blue-600" />,

    VIEW: <Eye className="w-4 text-blue-600" />,

    READ: <Eye className="w-4 text-blue-600" />,

    UPDATE: <Pencil className="w-4 text-blue-600" />,

    DELETE: <Trash2 className="w-4 text-blue-600" />,

    RESTORE: <RotateCcw className="w-4 text-blue-600" />,
  };

  return icons[code] || null;
}
