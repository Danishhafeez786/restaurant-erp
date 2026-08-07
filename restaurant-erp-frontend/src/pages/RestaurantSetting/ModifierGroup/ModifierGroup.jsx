import ModifierGroupTable from "./ModifierGroupTable";
import { useState } from "react";

export default function ModifierGroup() {
   const [collapsed, setCollapsed] = useState(() => {
      const saved = localStorage.getItem("sidebarCollapsed");
  
      if (saved === null || saved === "undefined") {
        return true; // default collapsed
      }
  
      return JSON.parse(saved);
    });
  return (
    <div className="flex-1 p-4 md:p-6 overflow-y-auto">
      <ModifierGroupTable/>
    </div>
  );
}